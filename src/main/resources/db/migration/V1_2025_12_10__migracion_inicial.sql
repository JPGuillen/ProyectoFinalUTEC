CREATE TABLE usuarios (
    id_usuario SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(20) CHECK (rol IN ('cliente', 'vendedor')) NOT NULL,
    nombre_publico_tienda VARCHAR(150),
    estado VARCHAR(20) CHECK (estado IN ('activo','inactivo')) DEFAULT 'activo',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_tienda_por_rol
        CHECK (
            (rol = 'vendedor' AND nombre_publico_tienda IS NOT NULL)
            OR
            (rol = 'cliente' AND nombre_publico_tienda IS NULL)
        )
);

CREATE TABLE categorias (
    id_categoria SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
	);

CREATE TABLE productos (
    id_producto SERIAL PRIMARY KEY,
    id_vendedor INTEGER NOT NULL,
    id_categoria INTEGER,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    imagen_url VARCHAR(255),  			-- Este reemplaza la tabla ProductoImagenes
--    activo BOOLEAN DEFAULT TRUE,      -- No es necesario x ahora
    FOREIGN KEY (id_vendedor) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria) ON DELETE SET NULL
);

CREATE TABLE carrito (
    id_carrito SERIAL PRIMARY KEY,
    id_usuario INTEGER NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

-- 4.1 Tabla: CARRITO_DETALLE (Items del carrito)

CREATE TABLE carrito_detalle (
    id_detalle SERIAL PRIMARY KEY,
    id_carrito INTEGER NOT NULL,
    id_producto INTEGER NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
--    precio_unitario DECIMAL(10,2) NOT NULL,			-- SE OBTENDRA DIRECTO DE PRODUCTOS
--    subtotal DECIMAL(10,2) GENERATED ALWAYS AS (cantidad * precio_unitario) STORED, -- SE OBTENDRA DIRECTO DE PRODUCTOS
    FOREIGN KEY (id_carrito) REFERENCES carrito(id_carrito) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE CASCADE
);

CREATE TABLE pedidos (
    id_pedido SERIAL PRIMARY KEY,
    id_cliente INTEGER NOT NULL,
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL,
    estado VARCHAR(20) CHECK (estado IN ('pendiente','pagado','enviado','cancelado','completado')) DEFAULT 'pendiente',
    direccion_envio TEXT NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);
-- 5.1 Tabla: PEDIDO_DETALLE
CREATE TABLE pedido_detalle (
    id_detalle SERIAL PRIMARY KEY,
    id_pedido INTEGER NOT NULL,
    id_producto INTEGER NOT NULL,
    id_vendedor INTEGER NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_unitario DECIMAL(10,2) NOT NULL,
 --   subtotal DECIMAL(10,2) GENERATED ALWAYS AS (cantidad * precio_unitario) STORED,
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE CASCADE,
    FOREIGN KEY (id_vendedor) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

CREATE TABLE tipo_pagos (
    id_pago SERIAL PRIMARY KEY,
    id_pedido INTEGER NOT NULL,
    metodo VARCHAR(50) NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    estado_pago VARCHAR(20) CHECK (estado_pago IN ('pendiente','completado','fallido')) DEFAULT 'pendiente',
    fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido) ON DELETE CASCADE
);

CREATE TABLE notificaciones (
    id_notificacion SERIAL PRIMARY KEY,
    id_usuario INTEGER NOT NULL,
    mensaje TEXT NOT NULL,
    leido BOOLEAN DEFAULT FALSE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

CREATE TABLE resenas (
    id_reseña SERIAL PRIMARY KEY,
    id_producto INTEGER NOT NULL,
    id_usuario INTEGER NOT NULL,
    calificacion INT CHECK (calificacion BETWEEN 1 AND 5) NOT NULL,
    comentario TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE CASCADE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);


