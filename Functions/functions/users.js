/**
 * 
 * Representa las operaciones relacionadas con los usuarios, ya sea administradores o clientes.
 * 
 * @author Alma Teresa Carpio Revilla
 * @author Mariana Marzayani Hernández
 */

const { onRequest } = require("firebase-functions/v2/https");
const logger = require("firebase-functions/logger");
const admin = require("firebase-admin");
const cors = require("cors");
const corsHandler = cors({ origin: true });


// Endpoint para verificar si un usuario es administrador
exports.checkAdmin = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      try {
        const authHeader = req.headers.authorization;
        logger.info("Verificando token y rol de administrador...");
  
        if (!authHeader || !authHeader.startsWith('Bearer ')) {
          return res.status(401).send({ message: "No autenticado. Faltante o inválido token." });
        }
        const idToken = authHeader.split('Bearer ')[1];
        const decodedToken = await admin.auth().verifyIdToken(idToken);
  
        const { uid } = decodedToken;
        const adminDoc = await admin.firestore().collection('administradores').doc(uid).get();
  
        if (!adminDoc.exists) {
          return res.status(404).send({ message: "Usuario no es administrador" });
        }
        return res.status(200).send({ message: "Usuario es administrador" });
  
      } catch (error) {
        console.error("Error verificando token o rol de administrador:", error);
        return res.status(500).send({ message: "Error verificando autenticación o rol de administrador", error: error.toString() });
      }
    });
  });
  
// Endpoint para obtener el uID (de fire authentication) de un usuario por su email
exports.getUidByEmail = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      try {
        const { email = null } = req.query;
  
        if (!email) {
          return res.status(400).send({ message: "Email faltante" });
        }
  
        const userRecord = await admin.auth().getUserByEmail(email);
  
        if (!userRecord) {
          return res.status(404).send({ message: "Usuario no encontrado" });
        }
  
        return res.status(200).send({ uid: userRecord.uid });
      } catch (error) {
        logger.error("Error obteniendo uID por email:", error);
        return res.status(500).send({ message: "Error obteniendo uID por email", error: error.toString() });
      }
    });
  });
  
// Registro de un nuevo usuarios
exports.registerUser = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      try {
        const { email, password, nombres, apellidos, telefono } = req.body;
  
        if (!email || !password || !nombres || !apellidos || !telefono) {
          return res.status(400).send({ message: "Datos incompletos o inválidos" });
        }
  
        foto_perfil = null;
  
        // Crear un nuevo usuario en Firebase Authentication
        const userRecord = await admin.auth().createUser({
          email,
          password,
        });
  
        // Crear un documento en la colección de usuarios
        const userData = {
          uid: userRecord.uid,
          email,
          nombres,
          apellidos,
          telefono,
          foto_perfil,
          carrito_activo: null,
          pedidos: []
        };
  
        await admin.firestore().collection('usuarios').doc(userRecord.uid).set(userData);
  
        // Crear un carrito activo para el usuario
        const cartData = {
          uid: userRecord.uid,
          estado: true,
          productos: []
        };
  
        const cartDoc = await admin.firestore().collection('carritos').add(cartData);
        await admin.firestore().collection('usuarios').doc(userRecord.uid).update({ carrito_activo: cartDoc.id });
  
        return res.status(201).send({ message: "Usuario registrado exitosamente", user: userData });
      } catch (error) {
        console.error("Error registrando usuario:", error);
        return res.status(500).send({ message: "Error registrando usuario", error: error.message });
      }
    });
  });
  
//Endpoint para obtener los datos de usuario por su uID, los datos únicamente serían: nombres, apellidos, telefono, email y foto_perfil. NO debe devolver el uID, carrito_activo
exports.getUserProfileByID = onRequest(async (req, res) => {
    cors(req, res, async () => {
      try {
        const { uid = null } = req.query;
  
        if (!uid) {
          return res.status(400).send({ message: "uID faltante" });
        }
  
        const userQuerySnapshot = await admin.firestore().collection('usuarios').where('uid', '==', uid).get();
  
        if (userQuerySnapshot.empty) {
          return res.status(404).send({ message: "Usuario no encontrado" });
        }
  
        // Asumimos que solo hay un usuario con el uID proporcionado
        const userDoc = userQuerySnapshot.docs[0];
        const userData = {
          nombres: userDoc.data().nombres,
          apellidos: userDoc.data().apellidos,
          telefono: userDoc.data().telefono,
          email: userDoc.data().email,
          foto_perfil: userDoc.data().foto_perfil,
        };
  
        return res.status(200).send(userData);
      } catch (error) {
        console.error("Error obteniendo datos de usuario por uID:", error);
        return res.status(500).send({ message: "Error obteniendo datos de usuario por uID", error: error.message });
      }
    });
  });
  
//Endpoint para actualizar los datos de un usuario por su uID, los datos a actualizar podrían ser: nombres, apellidos, telefono, email, contraseña y foto_perfil. No tiene que ser obligatorio actualizar todos, se pueden mantener los datos actuales.
// NO se debe permitir actualizar el uID, carrito_activo.
exports.updateUserProfileByID = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      try {
        const { uid = null, nombres = null, apellidos = null, telefono = null, email = null, password = null, foto_perfil = null } = req.body;
  
        if (!uid) {
          return res.status(400).send({ message: "uID faltante" });
        }
  
        const userQuerySnapshot = await admin.firestore().collection('usuarios').where('uid', '==', uid).get();
  
        if (userQuerySnapshot.empty) {
          return res.status(404).send({ message: "Usuario no encontrado" });
        }
  
        // Asumimos que solo hay un usuario con el uID proporcionado
        const userDoc = userQuerySnapshot.docs[0];
        const userRef = userDoc.ref;
        const userData = userDoc.data();
  
        if (nombres) userData.nombres = nombres;
        if (apellidos) userData.apellidos = apellidos;
        if (telefono) userData.telefono = telefono;
        if (email) userData.email = email;
        if (foto_perfil) userData.foto_perfil = foto_perfil;
  
        if (password) {
          await admin.auth().updateUser(uid, { password });
        }
  
        await userRef.update(userData);
  
        return res.status(200).send({ message: "Datos de usuario actualizados exitosamente", userData });
      } catch (error) {
        console.error("Error actualizando datos de usuario por uID:", error);
        return res.status(500).send({ message: "Error actualizando datos de usuario por uID", error: error.message });
      }
    });
  });
  
//Endpoint para eliminar a un usuario por su uID, únicamente de Firebase Authentication
exports.deleteUserByID = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      try {
        const { uid = null } = req.query;
  
        if (!uid) {
          return res.status(400).send({ message: "uID faltante" });
        }
  
        await admin.auth().deleteUser(uid);
  
        return res.status(200).send({ message: "Usuario eliminado exitosamente" });
      } catch (error) {
        console.error("Error eliminando usuario por uID:", error);
        return res.status(500).send({ message: "Error eliminando usuario por uID", error: error.message });
      }
    });
  });
  
// Endpoint para obtener los datos de un usuarios por su uID de la coleccion de usuarios (uid es un campo de la coleccion)
exports.getUserData = onRequest(async (req, res) => {
    corsHandler(req, res, async () => {
      try {
        const { uid = null } = req.query;
  
        if (!uid) {
          return res.status(400).send({ message: "uID faltante" });
        }
  
        const userQuerySnapshot = await admin.firestore().collection('usuarios').where('uid', '==', uid).get();
  
        if (userQuerySnapshot.empty) {
          return res.status(404).send({ message: "Usuario no encontrado" });
        }
  
        // Asumimos que solo hay un usuario con el uID proporcionado
        const userDoc = userQuerySnapshot.docs[0];
        const userData = {
          id: userDoc.id,
          ...userDoc.data()
        };
  
        return res.status(200).send(userData);
      } catch (error) {
        logger.error("Error obteniendo datos de usuario:", error);
        return res.status(500).send({ message: "Error obteniendo datos de usuario", error: error.toString() });
      }
    });
  });