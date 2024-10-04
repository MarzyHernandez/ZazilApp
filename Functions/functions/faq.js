/**
 * 
 * Representa las operaciones relacionadas con las preguntas frecuentes (FAQ).
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

// Endopoint para obtener todas las peguntas frecuentes (FAQ)
exports.getAllFAQ = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      try {
        const faqSnapshot = await admin.firestore().collection('faq').get();
  
        if (faqSnapshot.empty) {
          logger.info("No se encontraron preguntas frecuentes.");
          return res.status(404).json({ message: "No se encontraron preguntas frecuentes." });
        }
  
        const faq = faqSnapshot.docs.map(doc => ({
          id: doc.id,
          ...doc.data()
        }));
  
        logger.info("Preguntas frecuentes obtenidas exitosamente:", faq);
        res.status(200).json(faq);
      } catch (error) {
        logger.error("Error obteniendo preguntas frecuentes:", error);
        res.status(500).json({ message: "Error obteniendo preguntas frecuentes", error: error.toString() });
      }
    });
  });
  
// Endpoint para agregar una pregunta frecuente (FAQ)
const faqCounterRef = admin.firestore().collection('counters').doc('faqCounter');
exports.addFAQ = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      if (req.method !== "POST") {
        return res.status(405).send({ message: "Método no permitido" });
      }
  
      try {
        // Incrementar el contador y obtener el nuevo ID
        const counterDoc = await faqCounterRef.get();
        let currentID = 1;
  
        if (counterDoc.exists) {
          currentID = counterDoc.data().currentID + 1;
          await faqCounterRef.update({ currentID });
        } else {
          await faqCounterRef.set({ currentID });
        }
  
        // Obtener y sanear los datos del cuerpo de la solicitud
        const { pregunta = null, respuesta = null } = req.body;
  
        const sanitizedPregunta = validator.escape(validator.trim(pregunta || ""));
        const sanitizedRespuesta = validator.escape(validator.trim(respuesta || ""));
  
        if (!sanitizedPregunta || !sanitizedRespuesta) {
          return res.status(400).send({ message: "Datos incompletos o inválidos" });
        }
  
        const faqData = {
          id: currentID,
          pregunta: sanitizedPregunta,
          respuesta: sanitizedRespuesta
        };
  
        await admin.firestore().collection('faq').add(faqData);
        logger.info("Pregunta frecuente agregada exitosamente:", faqData);
  
        return res.status(201).send({ message: "Pregunta frecuente agregada exitosamente", faq: faqData });
      } catch (error) {
        logger.error("Error agregando pregunta frecuente:", error);
        return res.status(500).send({ message: "Error agregando pregunta frecuente", error: error.toString() });
      }
    });
  });
  
//Endpoint para eliminar una pregunta frecuenta
exports.deleteFAQ = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      if (req.method !== "DELETE") {
        return res.status(405).send({ message: "Método no permitido" });
      }
  
      try {
        const { id = null } = req.body;
  
        if (!id) {
          return res.status(400).send({ message: "ID de pregunta frecuente faltante" });
        }
  
        // Verificar si el id es un entero
        if (!Number.isInteger(id)) {
          return res.status(400).send({ message: "ID de pregunta frecuente debe ser un entero" });
        }
  
        // Buscar el documento donde el campo 'id' coincida con el valor proporcionado
        const faqQuerySnapshot = await admin.firestore().collection('faq').where('id', '==', id).get();
  
        if (faqQuerySnapshot.empty) {
          return res.status(404).send({ message: "Pregunta frecuente no encontrada" });
        }
  
        // Asumimos que solo hay un documento con el id proporcionado
        const faqDoc = faqQuerySnapshot.docs[0];
        const faqRef = faqDoc.ref;
  
        await faqRef.delete();
        logger.info("Pregunta frecuente eliminada exitosamente:", faqDoc.data());
  
        return res.status(200).send({ message: "Pregunta frecuente eliminada exitosamente" });
      } catch (error) {
        logger.error("Error eliminando pregunta frecuente:", error);
        return res.status(500).send({ message: "Error eliminando pregunta frecuente", error: error.toString() });
      }
    });
  });