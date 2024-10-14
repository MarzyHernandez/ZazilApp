//dashboard.js
//Este archivo contiene el código JavaScript para las gráficas de la página de dashboard del administrador.
//Autor: Carlos Herrera


// Función para obtener los datos del endpoint
// Autor: Carlos Herrera
// Parámetros: Ninguno
// Retorna: Un arreglo con los datos de las órdenes
async function fetchOrderData() {
    const response = await fetch('https://getallorders-dztx2pd2na-uc.a.run.app/');
    const data = await response.json();
    return data;
}

// Función principal para procesar y actualizar los datos en las gráficas y totales
// Autor: Carlos Herrera
// Parámetros: Ninguno
// Retorna: Ninguno
fetchOrderData().then(orders => {
    const monthlySales = Array(12).fill(0);    // Ventas por mes
    const monthlyOrders = Array(12).fill(0);   // Órdenes por mes
    const productSales = {};                   // Ventas por producto
    const stateOrders = {};                    // Pedidos por estado
    const paymentMethods = {};                 // Métodos de pago
    const usersPerMonth = Array(12).fill(0);   // Usuarios nuevos por mes

    let totalSales = 0;                        // Total de ventas
    let totalOrders = 0;                       // Total de órdenes
    let pendingOrders = 0;                     // Órdenes pendientes
    let shippedOrders = 0;                     // Órdenes enviadas
    let deliveredOrders = 0;                   // Órdenes entregadas
    orders.forEach(order => {
        const orderDate = new Date(order.fecha_pedido);
        const month = orderDate.getMonth();

        // Ventas y órdenes por mes
        monthlySales[month] += order.monto_total;
        monthlyOrders[month] += 1;

        // Sumar total de ventas
        totalSales += order.monto_total;

        // Sumar número total de órdenes
        totalOrders += 1;
        
        // Contar órdenes según el estado
        if (order.estado === "Pendiente" || order.estado === "pendiente") {
            pendingOrders += 1;
        } else if (order.estado === "Enviado") {
            shippedOrders += 1;
        } else if (order.estado === "Entregado") {
            deliveredOrders += 1;
        }
    
        // Estado de origen del usuario
        const state = order.estado;
        if (!stateOrders[state]) {
            stateOrders[state] = 0;
        }
        stateOrders[state] += 1;

        // Productos más vendidos
        order.productos.forEach(product => {
            if (!productSales[product.nombre]) {
                productSales[product.nombre] = { sales: 0, quantity: 0 };
            }
            productSales[product.nombre].sales += product.precio * product.cantidad;
            productSales[product.nombre].quantity += product.cantidad;
        });

        // Método de pago
        const paymentMethod = order.metodo_pago;
        if (!paymentMethods[paymentMethod]) {
            paymentMethods[paymentMethod] = 0;
        }
        paymentMethods[paymentMethod] += 1;
    });

    // Actualizar las gráficas
    updateSalesGraph(monthlySales);
    updateOrdersGraph(monthlyOrders);
    updateProductSalesGraph(productSales);
    updateProductRevenueGraph(productSales);
    updateUsersGraph(usersPerMonth);
    updatePaymentMethodGraph(paymentMethods);
    updateStateOrdersGraph(stateOrders);

    // Actualizar los valores en la página
    document.getElementById('totalSales').textContent = totalSales.toFixed(2);  // Mostrar total de ventas con dos decimales
    document.getElementById('delieveredOrders').textContent = deliveredOrders;
    document.getElementById('pendingOrders').textContent = pendingOrders;
    document.getElementById('shippedOrders').textContent = shippedOrders;
});

// FunciON para actualizar las gráfica DE ventas totales por mes
// Autor: Carlos Herrera
// Parámetros: Un arreglo con los datos de las órdenes
// Retorna: Ninguno

// 1. Ventas Totales por Mes
function updateSalesGraph(monthlySales) {
    var options = {
        chart: {
            type: 'bar',
            height: 300
        },
        series: [{
            name: 'Ventas Totales',
            data: monthlySales
        }],
        xaxis: {
            categories: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
            labels: {
                style: {
                    fontFamily: 'Gabarito, sans-serif'  
                }
            }
        },
        yaxis: {
            labels: {
                style: {
                    fontFamily: 'Gabarito, sans-serif' 
                }
            }
        },
        colors: ['#E17F61'],
        plotOptions: {
            bar: {
                borderRadius: 10 
            }
        },
        
        theme: {
            mode: 'light',
            palette: 'palette1'
        }
    };

    var chart = new ApexCharts(document.querySelector("#salesGraph"), options);
    chart.render();
}

// 2. Órdenes por Mes

// Función para actualizar la gráfica de órdenes por mes
// Autor: Carlos Herrera
// Parámetros: Un arreglo con los datos de las órdenes
// Retorna: Ninguno
function updateOrdersGraph(monthlyOrders) {
    var options = {
        chart: {
            type: 'line',
            height: 300
        },
        series: [{
            name: 'Órdenes',
            data: monthlyOrders
        }],
        xaxis: {
            categories: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
            labels: {
                style: {
                    fontFamily: 'Gabarito, sans-serif'  
                }
            }
        },
        yaxis: {
            labels: {
                style: {
                    fontFamily: 'Gabarito, sans-serif'  
                }
            }
        },
        colors: ['#EBB7A7'],
        
    };

    var chart = new ApexCharts(document.querySelector("#ordersGraph"), options);
    chart.render();
}

// 3. Productos más Vendidos

// Función para actualizar la gráfica de productos más vendidos
// Autor: Carlos Herrera
// Parámetros: Un objeto con los datos de las órdenes
// Retorna: Ninguno
function updateProductSalesGraph(productSales) {
    var productNames = Object.keys(productSales);
    var productQuantities = productNames.map(name => productSales[name].quantity);

    var options = {
        chart: {
            type: 'bar',
            height: 300
        },
        series: [{
            name: 'Productos Vendidos',
            data: productQuantities
        }],
        xaxis: {
            categories: productNames,
            labels: {
                style: {
                    fontFamily: 'Gabarito, sans-serif'  // Fuente
                },
                
            }
        },
        yaxis: {
            labels: {
                style: {
                    fontFamily: 'Gabarito, sans-serif'  // Fuente en los números de los ejes
                }
            }
        },
        colors: ['#FEE1D6'],
        plotOptions: {
            bar: {
                borderRadius: 10 // Bordes redondeados
            }
        },
    };

    var chart = new ApexCharts(document.querySelector("#productSalesGraph"), options);
    chart.render();
}

// 4. Ingresos por Producto

// Función para actualizar la gráfica de ingresos por producto
// Autor: Carlos Herrera
// Parámetros: Un objeto con los datos de las órdenes
// Retorna: Ninguno
function updateProductRevenueGraph(productSales) {
    var productNames = Object.keys(productSales);
    var productRevenues = productNames.map(name => productSales[name].sales);

    var options = {
        chart: {
            type: 'bar',
            height: 300
        },
        series: [{
            name: 'Ingresos por Producto',
            data: productRevenues
        }],
        xaxis: {
            categories: productNames,
            labels: {
                style: {
                    fontFamily: 'Gabarito, sans-serif'  // Fuente en los números de los ejes
                }
            }
        },
        yaxis: {
            labels: {
                style: {
                    fontFamily: 'Gabarito, sans-serif'  // Fuente en los números de los ejes
                }
            }
        },
        colors: ['#EBB7A7'],
        plotOptions: {
            bar: {
                borderRadius: 10 // Bordes redondeados
            }
        },
        
    };

    var chart = new ApexCharts(document.querySelector("#productRevenueGraph"), options);
    chart.render();
}

// 5. Usuarios Nuevos por Mes

// Función para actualizar la gráfica de usuarios nuevos por mes
// Autor: Carlos Herrera
// Parámetros: Un arreglo con los datos de las órdenes
// Retorna: Ninguno
function updateUsersGraph(usersPerMonth) {
    var options = {
        chart: {
            type: 'line',
            height: 300
        },
        series: [{
            name: 'Usuarios Nuevos',
            data: usersPerMonth
        }],
        xaxis: {
            categories: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
            labels: {
                style: {
                    fontFamily: 'Gabarito, sans-serif'  // Fuente en los números de los ejes
                }
            }
        },
        yaxis: {
            labels: {
                style: {
                    fontFamily: 'Gabarito, sans-serif'  // Fuente en los números de los ejes
                }
            }
        },
        colors: ['#E17F61'],
     
    };

    var chart = new ApexCharts(document.querySelector("#usersGraph"), options);
    chart.render();
}

// 6. Distribución de Métodos de Pago

// Función para actualizar la gráfica de distribución de métodos de pago
// Autor: Carlos Herrera
// Parámetros: Un objeto con los datos de las órdenes
// Retorna: Ninguno

function updatePaymentMethodGraph(paymentMethods) {
    var paymentNames = Object.keys(paymentMethods);
    var paymentCounts = paymentNames.map(name => paymentMethods[name]);

    var options = {
        chart: {
            type: 'pie',
            height: 300
        },
        series: paymentCounts,
        labels: paymentNames,
        colors: ['#FEE1D6', '#EBB7A7', '#E17F61'],
        title: {
            text: 'Distribución de Métodos de Pago',
            align: 'center',
            style: {
                fontFamily: 'Gabarito, sans-serif'
            }
        },
        dataLabels: {
            style: {
                fontFamily: 'Gabarito, sans-serif'  // Fuente en las etiquetas de la serie
            }
        }
    };

    var chart = new ApexCharts(document.querySelector("#paymentMethodGraph"), options);
    chart.render();
}

// 7. Distribución de Pedidos por Estado

// Función para actualizar la gráfica de distribución de pedidos por estado
// Autor: Carlos Herrera
// Parámetros: Un objeto con los datos de las órdenes
// Retorna: Ninguno

function updateStateOrdersGraph(stateOrders) {
    var stateNames = Object.keys(stateOrders);
    var stateQuantities = stateNames.map(name => stateOrders[name]);

    var options = {
        chart: {
            type: 'bar',
            height: 300
        },
        series: [{
            name: 'Cantidad de Pedidos',
            data: stateQuantities
        }],
        xaxis: {
            categories: stateNames,
            labels: {
                style: {
                    fontFamily: 'Gabarito, sans-serif'  // Fuente en los números de los ejes
                }
            }
        },
        yaxis: {
            labels: {
                style: {
                    fontFamily: 'Gabarito, sans-serif'  // Fuente en los números de los ejes
                }
            }
        },
        colors: ['#E17F61'],
        plotOptions: {
            bar: {
                borderRadius: 10 // Bordes redondeados
            }
        },
        
    };

    var chart = new ApexCharts(document.querySelector("#stateOrdersGraph"), options);
    chart.render();
}