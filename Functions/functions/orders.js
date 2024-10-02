/**
 * 
 * Representa las operaciones relacionadas con los pedidos de la tienda.
 * 
 * @author Alma Teresa Carpio Revilla
 * @author Mariana Marzayani Hernández
 */

const { onRequest } = require("firebase-functions/v2/https");
const logger = require("firebase-functions/logger");
const admin = require("firebase-admin");
const cors = require("cors");
const { defineSecret } = require('firebase-functions/params');
const stripeSecret = defineSecret('STRIPE_SECRET_KEY');
const stripe = require('stripe');
const paypalSdk = require("@paypal/checkout-server-sdk");
const paypalClient = defineSecret('PAYPAL_CLIENT_ID');
const paypalSecret = defineSecret('PAYPAL_CLIENT_SECRET');
const mail_pass = defineSecret('MAIL_PASS');
const mail_user = defineSecret('MAIL_USER');
const nodemailer = require('nodemailer');
const corsHandler = cors({ origin: true });

//Endpoint para obtener todos los pedidos
exports.getAllOrders = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      try {
        // Log para verificar que la función se está ejecutando
        logger.info("Obteniendo pedidos 2...");
        console.log("Obteniendo pedidos...");
  
        const ordersSnapshot = await admin.firestore().collection('pedidos').get();
  
        if (ordersSnapshot.empty) {
          logger.info("No se encontraron pedidos.");
          return res.status(404).json({ message: "No se encontraron pedidos." });
        }
  
        const orders = ordersSnapshot.docs.map(doc => {
          const data = doc.data();
  
          // Convertir el campo fecha a formato ISO 8601 si existe
          if (data.fecha_pedido) {
            data.fecha_pedido = data.fecha_pedido.toDate().toISOString();
          }
  
          if (data.fecha_entrega) {
            data.fecha_entrega = data.fecha_entrega.toDate().toISOString();
          }
  
          if (data.fecha_envio) {
            data.fecha_envio = data.fecha_envio.toDate().toISOString();
          }
  
          return {
            id: doc.id,
            ...data
          };
        });
  
        logger.info("Pedidos obtenidos exitosamente:", orders);
        res.status(200).json(orders);
      } catch (error) {
        logger.error("Error obteniendo pedidos:", error);
        res.status(500).json({ message: "Error obteniendo pedidos", error: error.toString() });
      }
    });
  });
  
// Endpoint para hacer un pedido
exports.makeOrder = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      try {
        const { uid, codigo_postal, estado, ciudad, calle, numero_interior, pais, colonia, metodo_pago } = req.body;
  
        if (!uid || !codigo_postal || !estado || !ciudad || !calle || !pais || !colonia || !metodo_pago) {
          return res.status(400).send({ message: "Datos incompletos o inválidos" });
        }
  
        // Buscar el carrito activo del usuario
        const cartQuerySnapshot = await admin.firestore().collection('carritos').where('uid', '==', uid).where('estado', '==', true).get();
  
        if (cartQuerySnapshot.empty) {
          return res.status(404).send({ message: "Carrito no encontrado" });
        }
  
        // Asumimos que solo hay un carrito activo por usuario
        const cartDoc = cartQuerySnapshot.docs[0];
        const cartRef = cartDoc.ref;
        const cartData = cartDoc.data();
  
        // Crear un nuevo pedido
        const orderCounterRef = admin.firestore().collection('counters').doc('orderCounter');
        const counterDoc = await orderCounterRef.get();
        let currentID = 1;
  
        if (counterDoc.exists) {
          currentID = counterDoc.data().currentID + 1;
          await orderCounterRef.update({ currentID });
        } else {
          await orderCounterRef.set({ currentID });
        }
  
        const orderData = {
          id: currentID,
          direccion_envio: {
            codigo_postal,
            estado,
            ciudad,
            calle,
            numero_interior,
            pais,
            colonia
          },
          fecha_pedido: admin.firestore.FieldValue.serverTimestamp(),
          estado: "pendiente",
          monto_total: cartData.monto_total,
          fecha_envio: null,
          fecha_entrega: null,
          metodo_pago,
          productos: [],
          uid: cartData.uid // Registrar el uid del carrito en el pedido
        };
  
        // Obtener los datos de los productos del carrito y verificar stock
        for (const product of cartData.productos) {
          const productQuerySnapshot = await admin.firestore().collection('productos').where('id', '==', product.id_producto).get();
  
          if (productQuerySnapshot.empty) {
            return res.status(404).send({ message: `Producto con id ${product.id_producto} no encontrado` });
          }
  
          const productDoc = productQuerySnapshot.docs[0].data();
  
          // Verificar si hay suficiente stock
          if (productDoc.stock < product.cantidad) {
            return res.status(400).send({ message: `Stock insuficiente para el producto ${productDoc.nombre}` });
          }
  
          // Verificar que los campos necesarios no sean undefined
          if (productDoc.precio_rebajado === undefined || productDoc.imagen === undefined || productDoc.nombre === undefined) {
            return res.status(400).send({ message: `Datos incompletos para el producto ${productDoc.nombre}` });
          }
  
          orderData.productos.push({
            precio: productDoc.precio_rebajado,
            imagen: productDoc.imagen,
            cantidad: product.cantidad,
            nombre: productDoc.nombre
          });
        }
  
        // Guardar el pedido en Firestore
        await admin.firestore().collection('pedidos').doc(currentID.toString()).set(orderData);
  
        // Cambiar el estado del carrito actual a false
        await cartRef.update({ estado: false });
  
        // Crear un nuevo carrito vacío para el usuario con estado true
        const nuevoCarrito = {
          uid,
          estado: true,
          monto_total: 0,
          productos: []
        };
        await admin.firestore().collection('carritos').add(nuevoCarrito);
  
        // Enviar la respuesta con el nuevo pedido creado
        res.json(orderData);
      } catch (error) {
        console.error("Error haciendo pedido:", error);
        return res.status(500).send({ message: "Error haciendo pedido", error: error.message });
      }
    });
  });

//Endpoint getOrderByID Recibir uid y devolver todos los pedidos de ese usuario
exports.getOrderByID = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      try {
        const { uid = null } = req.query;
  
        if (!uid) {
          return res.status(400).send({ message: "uID faltante" });
        }
  
        const ordersQuerySnapshot = await admin.firestore().collection('pedidos').where('uid', '==', uid).get();
  
        if (ordersQuerySnapshot.empty) {
          return res.status(404).send({ message: "Pedidos no encontrados" });
        }
  
        const orders = ordersQuerySnapshot.docs.map(doc => {
          const data = doc.data();
  
          // Convertir el campo fecha_pedido a formato ISO 8601 si existe
          if (data.fecha_pedido) {
            data.fecha_pedido = data.fecha_pedido.toDate().toISOString();
          }
  
          if (data.fecha_entrega) {
            data.fecha_entrega = data.fecha_entrega.toDate().toISOString();
          }
  
          if (data.fecha_envio) {
            data.fecha_envio = data.fecha_envio.toDate().toISOString();
          }
  
          return {
            id: doc.id,
            ...data
          };
        });
  
        return res.status(200).send(orders);
      } catch (error) {
        console.error("Error obteniendo pedidos por uID:", error);
        return res.status(500).send({ message: "Error obteniendo pedidos por uID", error: error.message });
      }
    });
  });

// Endpoint para enviar un correo llamado notifyChange
exports.notifyChange = onRequest({
  secrets: [mail_user, mail_pass],
}, async (req, res) => {
  corsHandler(req, res, async () => {
    // Validamos que la solicitud sea un POST
    if (req.method !== 'POST') {
      return res.status(405).send('Método no permitido');
    }

    const { correo, estado, id_pedido } = req.body;

    // Validamos los datos de entrada
    if (!correo || !estado || !id_pedido) {
      return res.status(400).send('Faltan datos requeridos: correo, estado, número de orden o id de pedido.');
    }

    try {
      const user = mail_user.value();
      const pass = mail_pass.value();

      // Configuramos el transportador de Nodemailer con MailerSend
      const transporter = nodemailer.createTransport({
        host: 'smtp.mailersend.net', 
        port: 587, 
        auth: {
          user: user, 
          pass: pass 
        }
      });

      // Buscar el pedido en la base de datos y actualizar su estado
      const pedidoRef = admin.firestore().collection('pedidos').doc(id_pedido);
      const pedidoDoc = await pedidoRef.get();

      if (!pedidoDoc.exists) {
        return res.status(404).send('Pedido no encontrado');
      }

      await pedidoRef.update({ estado });

      // Opciones del correo
      const mailOptions = {
        from: user, // Remitente
        to: correo, // Destinatario
        subject: `Actualización de Pedido #${id_pedido}`, 
        html: `
          <h1 style="font-family: Arial, sans-serif; color: #333;">Actualización de Pedido</h1>
          <p style="font-family: Arial, sans-serif; color: #555;">
            Estimado cliente,<br><br>
            Nos complace informarte que el estado de tu pedido <strong>#${id_pedido}</strong> ha sido actualizado a: 
            <strong style="color: #007BFF;">${estado}</strong>.<br><br>
            Gracias por confiar en nosotros para tus compras en línea.<br>
            Si tienes alguna pregunta, no dudes en contactarnos.
          </p>
          <p style="font-family: Arial, sans-serif; color: #555;">
            Atentamente,<br>
            El equipo Zazil.
          </p>
        `,
      };

      // Enviar el correo
      await transporter.sendMail(mailOptions);
      logger.info(`Correo enviado a ${correo} sobre el pedido #${id_pedido}`);
      logger.info(`Estado del pedido: ${estado}`);
      return res.status(200).send('Correo enviado con éxito y estado del pedido actualizado');
    } catch (error) {
      logger.error('Error al enviar el correo o actualizar el estado del pedido:', error);
      return res.status(500).send({
        message: 'Error al enviar el correo o actualizar el estado del pedido',
        error: error.message,
        user: mail_user.value(),
        pass: mail_pass.value()
      });
    }
  });
});
  
// Endpoint para procesar un pago con Stripe
exports.paymentSheet = onRequest({
    secrets: [stripeSecret],  // Declarar que usaremos el secreto
  }, async (req, res) => {
  
    const amount = req.body.amount;
  
    corsHandler(req, res, async () => {
      try {
  
        // Inicializar Stripe con la clave secreta
        const stripeInstance = stripe(stripeSecret.value());
  
        // Use an existing Customer ID if this is a returning customer.
        const customer = await stripeInstance.customers.create();
        const ephemeralKey = await stripeInstance.ephemeralKeys.create(
          { customer: customer.id },
          { apiVersion: '2024-06-20' }
        );
        const paymentIntent = await stripeInstance.paymentIntents.create({
          amount: amount * 100,
          currency: 'mxn',
          customer: customer.id,
          automatic_payment_methods: {
            enabled: true,
          },
        });
  
        res.json({
          paymentIntent: paymentIntent.client_secret,
          ephemeralKey: ephemeralKey.secret,
          customer: customer.id,
          publishableKey: 'pk_test_51PtEz32KeLmEQHLwr5TGLCLdKVLTd0FxV1ylYtTc0CnKk80GTnpSBX4qH6KzztagulHcv3au34U2e6j66ksWpMuQ00oHGXxuZ7'
        });
      } catch (error) {
        console.error('Error creating payment sheet:', error);
        res.status(500).send({ error: error.message });
      }
    });
  });
  
// Endpoint para procesar un pago con PayPal
exports.paypalPayment = onRequest({
    secrets: [paypalClient, paypalSecret],
  }, async (req, res) => {
    const amount = req.body.amount;
  
    corsHandler(req, res, async () => {
      try {
        // Acceder a los secretos de PayPal desde las variables de entorno
        const clientId = paypalClient.value();
        const clientSecret = paypalSecret.value();
  
        // Imprimir los valores de los secretos para depuración (solo en desarrollo)
        console.log('PayPal Client ID:', clientId);
        console.log('PayPal Client Secret:', clientSecret);
  
        // Inicializar el cliente de PayPal
        const paypalClientInstance = new paypalSdk.core.PayPalHttpClient(
          new paypalSdk.core.SandboxEnvironment(
            clientId,
            clientSecret
          )
        );
  
        // Crear una solicitud de orden de PayPal
        const request = new paypalSdk.orders.OrdersCreateRequest();
        request.prefer('return=representation');
        request.requestBody({
          intent: 'CAPTURE',
          purchase_units: [{
            amount: {
              currency_code: "MXN",
              value: amount
            }
          }]
        });
  
        // Ejecutar la solicitud de orden de PayPal
        const response = await paypalClientInstance.execute(request);
  
        // Enviar el ID de la orden a la aplicación cliente
        res.json({ orderID: response.result.id });
      } catch (error) {
        console.error('Error creating PayPal payment:', error);
        res.status(500).send({ error: error.message });
      }
    });
  });  