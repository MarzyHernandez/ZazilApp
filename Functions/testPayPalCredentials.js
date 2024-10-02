const paypalSdk = require('@paypal/checkout-server-sdk');

// Reemplaza estos valores con tus credenciales de PayPal
const clientId = "ASPhuxb86z1Rkft0V1DUaqHQD3Olgqx_22UoznTgBYbzp5AD2pC4Ovx3HWbr4uEp-RbEC4gE6Sf60kgy";
const clientSecret = "EM62ZJ46dv7Jlak9yM3_eedDNHQ99gEUd9SfHc0NoT1a5zLUO9EUcQsONZcfKLOJFbQKo4bdlnimAOFF";

// Configurar el entorno de PayPal
const environment = new paypalSdk.core.SandboxEnvironment(clientId, clientSecret);
const client = new paypalSdk.core.PayPalHttpClient(environment);

async function testCredentials() {
  try {
    // Crear una solicitud de orden de PayPal
    const request = new paypalSdk.orders.OrdersCreateRequest();
    request.prefer('return=representation');
    request.requestBody({
      intent: 'CAPTURE',
      purchase_units: [{
        amount: {
          currency_code: "MXN",
          value: "100.00"
        }
      }]
    });

    // Ejecutar la solicitud de orden de PayPal
    const response = await client.execute(request);

    console.log('Order ID:', response.result.id);
    console.log('Credentials are valid.');
  } catch (error) {
    console.error('Error creating PayPal order:', error);
    console.error('Credentials are invalid.');
  }
}

testCredentials();