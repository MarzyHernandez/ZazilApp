/**
 * 
 * Representa las operaciones relacionadas con las publicaciones.
 * 
 * @author Alma Teresa Carpio Revilla
 * @author Mariana Marzayani Hernández
 */

// Llamar al archivo auxiliar.js
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

// Endpoint para obtener todas las publicaciones
exports.getAllPost = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      try {
        const postSnapshot = await admin.firestore().collection('post').get();
  
        if (postSnapshot.empty) {
          logger.info("No se encontraron publicaciones.");
          return res.status(404).json({ message: "No se encontraron publicaciones." });
        }
  
        const posts = postSnapshot.docs.map(doc => {
          const data = doc.data();
  
          // Convertir el campo fecha a formato ISO 8601 si existe
          if (data.fecha) {
            data.fecha = data.fecha.toDate().toISOString();
          }
  
          return {
            id: doc.id,
            ...data
          };
        });
  
        logger.info("Publicaciones obtenidas exitosamente:", posts);
        res.status(200).json(posts);
      } catch (error) {
        logger.error("Error obteniendo publicaciones:", error);
        res.status(500).json({ message: "Error obteniendo publicaciones", error: error.toString() });
      }
    });
  });
  
// Endpoint para agregar una publicación
  const postCounterRef = admin.firestore().collection('counters').doc('postCounter');
  const { Timestamp } = require('firebase-admin/firestore');
  const e = require("cors");
  exports.addPost = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      if (req.method !== "POST") {
        return res.status(405).send({ message: "Método no permitido" });
      }
  
      try {
        // Incrementar el contador y obtener el nuevo ID
        const counterDoc = await postCounterRef.get();
        let currentID = 1;
  
        if (counterDoc.exists) {
          currentID = counterDoc.data().currentID + 1;
          await postCounterRef.update({ currentID });
        } else {
          await postCounterRef.set({ currentID });
        }
  
        // Obtener y sanear los datos del cuerpo de la solicitud
        const { titulo = null,
          contenido = null,
          imagen = null,
          autor = null,
          fecha = null,
          categoria = null,
          meGusta = null } = req.body;
  
        const sanitizedTitulo = validator.escape(validator.trim(titulo || ""));
        const sanitizedContenido = validator.escape(validator.trim(contenido || ""));
        let sanitizedImagen = validator.escape(validator.trim(imagen || ""));
        const sanitizedAutor = validator.escape(validator.trim(autor || ""));
        const sanitizedCategoria = validator.escape(validator.trim(categoria || ""));
        const sanitizedMeGusta = validator.isInt(meGusta?.toString()) ? parseInt(meGusta) : null;
        const sanitizedFecha = fecha ? Timestamp.fromDate(new Date(fecha)) : null;
  
        if (!sanitizedTitulo || !sanitizedContenido || !sanitizedAutor || !sanitizedCategoria) {
          return res.status(400).send({ message: "Datos incompletos o inválidos" });
        }
  
        // Corrección del link de la imagen (decodificar entidades HTML)
        sanitizedImagen = decodeHTMLEntities(sanitizedImagen);
  
        const postData = {
          id: currentID,
          titulo: sanitizedTitulo,
          contenido: sanitizedContenido,
          imagen: sanitizedImagen,
          autor: sanitizedAutor,
          fecha: sanitizedFecha,
          categoria: sanitizedCategoria,
          meGusta: sanitizedMeGusta
        };
  
        await admin.firestore().collection('post').add(postData);
        logger.info("Publicación agregada exitosamente:", postData);
  
        return res.status(201).send({ message: "Publicación agregada exitosamente", post: postData });
      } catch (error) {
        logger.error("Error agregando publicación:", error);
        return res.status(500).send({ message: "Error agregando publicación", error: error.toString() });
      }
    });
  });
  
// Endpoint para eliminar una publicación
exports.deletePost = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      if (req.method !== "DELETE") {
        return res.status(405).send({ message: "Método no permitido" });
      }
  
      try {
        const { id = null } = req.body;
  
        if (!id) {
          return res.status(400).send({ message: "ID de publicación faltante" });
        }
  
        // Verificar si el id es un entero
        if (!Number.isInteger(id)) {
          return res.status(400).send({ message: "ID de publicación debe ser un entero" });
        }
  
        // Buscar el documento donde el campo 'id' coincida con el valor proporcionado
        const postQuerySnapshot = await admin.firestore().collection('post').where('id', '==', id).get();
  
        if (postQuerySnapshot.empty) {
          return res.status(404).send({ message: "Publicación no encontrada" });
        }
  
        // Asumimos que solo hay un documento con el id proporcionado
        const postDoc = postQuerySnapshot.docs[0];
        const postRef = postDoc.ref;
  
        await postRef.delete();
        logger.info("Publicación eliminada exitosamente:", postDoc.data());
  
        return res.status(200).send({ message: "Publicación eliminada exitosamente" });
      } catch (error) {
        logger.error("Error eliminando publicación:", error);
        return res.status(500).send({ message: "Error eliminando publicación", error: error.toString() });
      }
    });
  });