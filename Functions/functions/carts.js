/**
 * 
 * Representa las operaciones relacionadas con los carritos de la tienda.
 * 
 * @author Alma Teresa Carpio Revilla
 * @author Mariana Marzayani Hernández
 */


const { onRequest } = require("firebase-functions/v2/https");
const logger = require("firebase-functions/logger");
const admin = require("firebase-admin");
const cors = require("cors");
const corsHandler = cors({ origin: true });


// Endpoint para regresar un carrito activo (estado=true), por el uID del usuario. Si el carrito no existe, se crea uno nuevo y se regresa el vacio.
exports.getActiveCart = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      try {
        const { uid = null } = req.query;
  
        if (!uid) {
          return res.status(400).send({ message: "uID faltante" });
        }
  
        const cartQuerySnapshot = await admin.firestore().collection('carritos').where('uid', '==', uid).where('estado', '==', true).get();
  
        if (cartQuerySnapshot.empty) {
          // Crear un nuevo carrito
          const newCart = {
            uid,
            estado: true,
            productos: []
          };
  
          const newCartDoc = await admin.firestore().collection('carritos').add(newCart);
          const newCartData = {
            id: newCartDoc.id,
            ...newCart
          };
  
          return res.status(200).send(newCartData);
        }
  
        // Asumimos que solo hay un carrito activo por usuario
        const cartDoc = cartQuerySnapshot.docs[0];
        const cartData = {
          id: cartDoc.id,
          ...cartDoc.data()
        };
  
        return res.status(200).send(cartData);
      } catch (error) {
        logger.error("Error obteniendo carrito activo:", error);
        return res.status(500).send({ message: "Error obteniendo carrito activo", error: error.toString() });
      }
    });
  });
  
// Endpoint para actualizar un carrito 
exports.updateCart = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      if (req.method !== "PUT") {
        return res.status(405).send({ message: "Método no permitido" });
      }
  
      try {
        const { uid = null, id_producto = null, cantidad = null } = req.body;
  
        if (!uid || !id_producto || !cantidad) {
          return res.status(400).send({ message: "Datos incompletos o inválidos" });
        }
  
        // Verificar si el id_producto y cantidad son enteros
        if (!Number.isInteger(id_producto) || !Number.isInteger(cantidad)) {
          return res.status(400).send({ message: "ID de producto y cantidad deben ser enteros" });
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
  
        // Buscar el precio del producto donde el campo id sea igual a id_producto
        const productQuerySnapshot = await admin.firestore().collection('productos').where('id', '==', id_producto).get();
        if (productQuerySnapshot.empty) {
          return res.status(404).send({ message: "Producto no encontrado" });
        }
        const productDoc = productQuerySnapshot.docs[0];
        const productData = productDoc.data();
        const precio = productData.precio_rebajado || productData.precio_normal;
  
        const productIndex = cartData.productos.findIndex(product => product.id_producto === id_producto);
  
        if (productIndex === -1) {
          // Agregar un nuevo producto al carrito
          cartData.productos.push({ id_producto, cantidad });
          cartData.monto_total += cantidad * precio;
        } else {
          // Actualizar la cantidad de un producto existente
          const cantidadAnterior = cartData.productos[productIndex].cantidad;
          cartData.productos[productIndex].cantidad += cantidad;
          cartData.monto_total += (cartData.productos[productIndex].cantidad - cantidadAnterior) * precio;
  
          // Si la cantidad llega a 0, eliminar el producto del carrito
          if (cartData.productos[productIndex].cantidad <= 0) {
            cartData.productos.splice(productIndex, 1);
          }
        }
  
        await cartRef.update({ productos: cartData.productos, monto_total: cartData.monto_total });
        logger.info("Carrito actualizado exitosamente:", cartData);
  
        return res.status(200).send({ message: "Carrito actualizado exitosamente", cart: cartData });
      } catch (error) {
        logger.error("Error actualizando carrito:", error);
        return res.status(500).send({ message: "Error actualizando carrito", error: error.toString() });
      }
    });
  });