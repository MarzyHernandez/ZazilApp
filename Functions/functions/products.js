/**
 * 
 * Representa las operaciones relacionadas con los productos de la tienda.
 * 
 * @author Alma Teresa Carpio Revilla
 * @author Mariana Marzayani Hernández
 */

const { onRequest } = require("firebase-functions/v2/https");
const logger = require("firebase-functions/logger");
const admin = require("firebase-admin");
const validator = require("validator");
const cors = require("cors");
const corsHandler = cors({ origin: true });

// Función para decodificar las entidades HTML como &#x2F; a su valor original '/'
function decodeHTMLEntities(text) {
  const entitiesMap = {
    '&#x2F;': '/',
    '&#x3A;': ':',
    // Puedes agregar más entidades si es necesario
  };

  return text.replace(/&#x2F;|&#x3A;/g, match => entitiesMap[match]);
}

// Endpoint para obtener todos los productos
exports.getAllProducts = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {  // Manejar CORS
      try {
        // Log para verificar que la función se está ejecutando
        logger.info("Obteniendo productos 2...");
        console.log("Obteniendo productos...");
  
        const productsSnapshot = await admin.firestore().collection('productos').get();
  
        if (productsSnapshot.empty) {
          logger.info("No se encontraron productos.");
          return res.status(404).json({ message: "No se encontraron productos." });
        }
  
        const products = productsSnapshot.docs.map(doc => ({
          id: doc.id,
          ...doc.data()
        }));
  
        logger.info("Productos obtenidos exitosamente:", products);
        res.status(200).json(products);
      } catch (error) {
        logger.error("Error obteniendo productos:", error);
        res.status(500).json({ message: "Error obteniendo productos", error: error.toString() });
      }
    });
  });
  
// Endpoint para obtener un producto por ID
exports.getProductById = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      try {
        const { id = null } = req.query;
  
        if (!id) {
          return res.status(400).send({ message: "ID de producto faltante" });
        }
  
        // Verificar si el id es un entero
        if (!Number.isInteger(parseInt(id))) {
          return res.status(400).send({ message: "ID de producto debe ser un entero" });
        }
  
        // Buscar el documento donde el campo 'id' coincida con el valor proporcionado
        const productQuerySnapshot = await admin.firestore().collection('productos').where('id', '==', parseInt(id)).get();
  
        if (productQuerySnapshot.empty) {
          return res.status(404).send({ message: "Producto no encontrado" });
        }
  
        // Asumimos que solo hay un documento con el id proporcionado
        const productDoc = productQuerySnapshot.docs[0];
        const productData = productDoc.data();
  
        logger.info("Producto obtenido exitosamente:", productData);
        return res.status(200).send(productData);
      } catch (error) {
        logger.error("Error obteniendo producto:", error);
        return res.status(500).send({ message: "Error obteniendo producto", error: error.toString() });
      }
    });
  });
  
// Endpoint para agregar un producto
const counterRef = admin.firestore().collection('counters').doc('productCounter');  
exports.addProduct = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      if (req.method !== "POST") {
        return res.status(405).send({ message: "Método no permitido" });
      }
  
      try {
        // Incrementar el contador y obtener el nuevo ID
        const counterDoc = await counterRef.get();
        let currentID = 1;
  
        if (counterDoc.exists) {
          currentID = counterDoc.data().currentID + 1;
          await counterRef.update({ currentID });
        } else {
          await counterRef.set({ currentID });
        }
  
        // Obtener y sanear los datos del cuerpo de la solicitud
        const { descripcion = null,
          id_categoria = null,
          imagen = null,
          precio_normal = null,
          cantidad = null,
          nombre = null,
          precio_rebajado = null } = req.body;
  
        const sanitizedNombre = validator.escape(validator.trim(nombre || ""));
        const sanitizedDescripcion = validator.escape(validator.trim(descripcion || ""));
        let sanitizedImagen = validator.escape(validator.trim(imagen || ""));
        const sanitizedIdCategoria = validator.escape(validator.trim(id_categoria || ""));
        const sanitizedPrecioNormal = validator.isFloat(precio_normal?.toString()) ? parseFloat(precio_normal) : null;
        const sanitizedPrecioRebajado = validator.isFloat(precio_rebajado?.toString()) ? parseFloat(precio_rebajado) : null;
        const sanitizedCantidad = validator.isInt(cantidad?.toString()) ? parseInt(cantidad) : null;
  
        if (!sanitizedNombre || !sanitizedPrecioNormal) {
          return res.status(400).send({ message: "Datos incompletos o inválidos" });
        }
  
        // Corrección del link de la imagen (decodificar entidades HTML)
        sanitizedImagen = decodeHTMLEntities(sanitizedImagen);
  
        const productData = {
          id: currentID,
          descripcion: sanitizedDescripcion,
          id_categoria: sanitizedIdCategoria,
          imagen: sanitizedImagen,
          precio_normal: sanitizedPrecioNormal,
          cantidad: sanitizedCantidad,
          nombre: sanitizedNombre,
          precio_rebajado: sanitizedPrecioRebajado
        };
  
        await admin.firestore().collection('productos').add(productData);
        logger.info("Producto agregado exitosamente:", productData);
  
        return res.status(201).send({ message: "Producto agregado exitosamente", product: productData });
      } catch (error) {
        logger.error("Error agregando producto:", error);
        return res.status(500).send({ message: "Error agregando producto", error: error.toString() });
      }
    });
  });
  
// Endpoint para eliminar un producto 
exports.deleteProduct = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      if (req.method !== "DELETE") {
        return res.status(405).send({ message: "Método no permitido" });
      }
  
      try {
        const { id = null } = req.body;
  
        if (!id) {
          return res.status(400).send({ message: "ID de producto faltante" });
        }
  
        // Verificar si el id es un entero
        if (!Number.isInteger(id)) {
          return res.status(400).send({ message: "ID de producto debe ser un entero" });
        }
  
        // Buscar el documento donde el campo 'id' coincida con el valor proporcionado
        const productQuerySnapshot = await admin.firestore().collection('productos').where('id', '==', id).get();
  
        if (productQuerySnapshot.empty) {
          return res.status(404).send({ message: "Producto no encontrado" });
        }
  
        // Asumimos que solo hay un documento con el id proporcionado
        const productDoc = productQuerySnapshot.docs[0];
        const productRef = productDoc.ref;
  
        await productRef.delete();
        logger.info("Producto eliminado exitosamente:", productDoc.data());
  
        return res.status(200).send({ message: "Producto eliminado exitosamente" });
      } catch (error) {
        logger.error("Error eliminando producto:", error);
        return res.status(500).send({ message: "Error eliminando producto", error: error.toString() });
      }
    });
  
  });
  
// Endpoint para modificar el precio y precio rebajado de un producto por ID
  exports.updateProductPrice = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      if (req.method !== "PUT") {
        return res.status(405).send({ message: "Método no permitido" });
      }
  
      try {
        const { id = null, precio_normal = null, precio_rebajado = null, cantidad = null } = req.body;
  
        if (!id || (precio_normal === null && precio_rebajado === null && cantidad === null)) {
          return res.status(400).send({ message: "Datos incompletos o inválidos" });
        }
  
        // Verificar si el id es un entero
        if (!Number.isInteger(id)) {
          return res.status(400).send({ message: "ID de producto debe ser un entero" });
        }
  
        // Verificar si los precios son números si se proporcionan
        if (precio_normal !== null && !validator.isFloat(precio_normal.toString())) {
          return res.status(400).send({ message: "Precio normal debe ser un número" });
        }
        if (precio_rebajado !== null && !validator.isFloat(precio_rebajado.toString())) {
          return res.status(400).send({ message: "Precio rebajado debe ser un número" });
        }
  
        // Verificar si la cantidad es un entero si se proporciona
        if (cantidad !== null && !Number.isInteger(cantidad)) {
          return res.status(400).send({ message: "Cantidad debe ser un entero" });
        }
  
        // Buscar el documento donde el campo 'id' coincida con el valor proporcionado
        const productQuerySnapshot = await admin.firestore().collection('productos').where('id', '==', id).get();
  
        if (productQuerySnapshot.empty) {
          return res.status(404).send({ message: "Producto no encontrado" });
        }
  
        // Asumimos que solo hay un documento con el id proporcionado
        const productDoc = productQuerySnapshot.docs[0];
        const productRef = productDoc.ref;
  
        // Crear un objeto con los campos a actualizar
        const updateData = {};
        if (precio_normal !== null) updateData.precio_normal = precio_normal;
        if (precio_rebajado !== null) updateData.precio_rebajado = precio_rebajado;
        if (cantidad !== null) updateData.cantidad = cantidad;
  
        await productRef.update(updateData);
        logger.info("Producto actualizado exitosamente:", productDoc.data());
  
        return res.status(200).send({ message: "Producto actualizado exitosamente" });
      } catch (error) {
        logger.error("Error actualizando producto:", error);
        return res.status(500).send({ message: "Error actualizando producto", error: error.toString() });
      }
    });
  });