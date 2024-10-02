/**
 * 
 * Index de las funciones de Firebase Cloud Functions.
 * 
 * @author Alma Teresa Carpio Revilla
 * @author Mariana Marzayani Hernández
 */


const admin = require("firebase-admin");
admin.initializeApp();

// Exportar las funciones de "productos" (products.js).
exports.getAllProducts = require('./functions/products').getAllProducts;
exports.getProductById = require('./functions/products').getProductById;
exports.addProduct = require('./functions/products').addProduct;
exports.deleteProduct = require('./functions/products').deleteProduct;
exports.updateProductPrice = require('./functions/products').updateProductPrice;

// Exportar las funciones de "preguntas frecuentes" (faq.js).
exports.getAllFAQ = require('./functions/faq').getAllFAQ;
exports.addFAQ = require('./functions/faq').addFAQ;
exports.deleteFAQ = require('./functions/faq').deleteFAQ;

// Exportar las funciones de "publicaciones" (posts.js).
exports.getAllPost = require('./functions/posts').getAllPost;
exports.addPost = require('./functions/posts').addPost;
exports.deletePost = require('./functions/posts').deletePost;

// Exportar las funciones de "carritos" (carts.js).
exports.getActiveCart = require('./functions/carts').getActiveCart;
exports.updateCart = require('./functions/carts').updateCart;

// Exportar las funciones de "usuarios" (users.js).
exports.checkAdmin = require('./functions/users').checkAdmin;
exports.getUidByEmail = require('./functions/users').getUidByEmail;
exports.registerUser = require('./functions/users').registerUser;
exports.getUserProfileByID = require('./functions/users').getUserProfileByID;
exports.updateUserProfileByID = require('./functions/users').updateUserProfileByID;
exports.deleteUserByID = require('./functions/users').deleteUserByID;
exports.getUserData = require('./functions/users').getUserData;

// Exportar las funciones de "pedidos" (orders.js).
exports.getAllOrders = require('./functions/orders').getAllOrders;
exports.makeOrder = require('./functions/orders').makeOrder;
exports.getOrderByID = require('./functions/orders').getOrderByID;
exports.notifyChange = require('./functions/orders').notifyChange;
exports.paymentSheet = require('./functions/orders').paymentSheet;
exports.paypalPayment = require('./functions/orders').paypalPayment;
