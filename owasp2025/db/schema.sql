DROP TABLE IF EXISTS usuario CASCADE;
DROP TABLE IF EXISTS rol CASCADE;
DROP TABLE IF EXISTS departamento CASCADE;
DROP TABLE IF EXISTS intentos_autenticacion CASCADE;


CREATE TABLE departamento (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE rol (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE usuario (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    primer_apellido VARCHAR(100) NOT NULL,
    segundo_apellido VARCHAR(100),
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(64) NOT NULL,
    telefono VARCHAR(20),
    fecha_nacimiento DATE,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMPTZ,
    departamento_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,

    FOREIGN KEY (departamento_id)
        REFERENCES departamento(id),

    FOREIGN KEY (rol_id)
        REFERENCES rol(id)
);

CREATE TABLE IF NOT EXISTS intentos_autenticacion (
    id BIGINT GENERATED ALWAYS AS IDENTITY,
    email VARCHAR(150) NOT NULL,
    intentos_fallidos INTEGER NOT NULL DEFAULT 0,
    fecha_primer_intento TIMESTAMP WITH TIME ZONE
        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_ultimo_intento TIMESTAMP WITH TIME ZONE
        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    alerta_enviada BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT intentos_autenticacion_pkey
        PRIMARY KEY (id),
    CONSTRAINT intentos_autenticacion_email_key
        UNIQUE (email)
);

-- Insertar 5 departamentos
INSERT INTO departamento (nombre, descripcion)
VALUES
    ('Recursos Humanos', 'Gestion del personal y contratacion'),
    ('Tecnologias de la Informacion', 'Administracion de sistemas y desarrollo de software'),
    ('Finanzas', 'Gestion financiera y contabilidad'),
    ('Ventas', 'Comercializacion de productos y servicios'),
    ('Marketing', 'Publicidad, comunicacion y estrategias de mercado');


-- INSERTAR 5 ROLES
INSERT INTO rol (nombre, descripcion)
VALUES
    ('Administrador', 'Gestion y administracion del sistema'),
    ('Desarrollador', 'Desarrollo y mantenimiento de aplicaciones'),
    ('Analista', 'Analisis de informacion y requerimientos'),
    ('Supervisor', 'Supervision de actividades y usuarios'),
    ('Usuario', 'Acceso general al sistema');



INSERT INTO usuario (
    nombre,
    primer_apellido,
    segundo_apellido,
    email,
    password_hash,
    telefono,
    fecha_nacimiento,
    departamento_id,
    rol_id
)
VALUES
(
    'Juan', 'Perez', 'Lopez',
    'juan.perez@ejemplo.com',
    'b460b1982188f11d175f60ed670027e1afdd16558919fe47023ecd38329e0b7f',
    '5512345678', '1990-04-15',
    (SELECT id FROM departamento WHERE nombre = 'Tecnologias de la Informacion'),
    (SELECT id FROM rol WHERE nombre = 'Administrador')
),
(
    'Maria', 'Garcia', 'Hernandez',
    'maria.garcia@ejemplo.com',
    'b460b1982188f11d175f60ed670027e1afdd16558919fe47023ecd38329e0b7f',
    '5523456789', '1995-08-22',
    (SELECT id FROM departamento WHERE nombre = 'Recursos Humanos'),
    (SELECT id FROM rol WHERE nombre = 'Usuario')
),
(
    'Carlos', 'Rodriguez', 'Martinez',
    'carlos.rodriguez@ejemplo.com',
    'b460b1982188f11d175f60ed670027e1afdd16558919fe47023ecd38329e0b7f',
    '5534567890', '1988-12-03',
    (SELECT id FROM departamento WHERE nombre = 'Tecnologias de la Informacion'),
    (SELECT id FROM rol WHERE nombre = 'Desarrollador')
),
(
    'Ana', 'Lopez', 'Sanchez',
    'ana.lopez@ejemplo.com',
    'b460b1982188f11d175f60ed670027e1afdd16558919fe47023ecd38329e0b7f',
    '5545678901', '1992-06-10',
    (SELECT id FROM departamento WHERE nombre = 'Finanzas'),
    (SELECT id FROM rol WHERE nombre = 'Analista')
),
(
    'Luis', 'Martinez', 'Ramirez',
    'luis.martinez@ejemplo.com',
    'b460b1982188f11d175f60ed670027e1afdd16558919fe47023ecd38329e0b7f',
    '5556789012', '1998-02-28',
    (SELECT id FROM departamento WHERE nombre = 'Ventas'),
    (SELECT id FROM rol WHERE nombre = 'Usuario')
),
(
    'Sofia', 'Hernandez', 'Torres',
    'sofia.hernandez@ejemplo.com',
    'b460b1982188f11d175f60ed670027e1afdd16558919fe47023ecd38329e0b7f',
    '5567890123', '1993-11-19',
    (SELECT id FROM departamento WHERE nombre = 'Marketing'),
    (SELECT id FROM rol WHERE nombre = 'Supervisor')
),
(
    'Pedro', 'Sanchez', 'Flores',
    'pedro.sanchez@ejemplo.com',
    'b460b1982188f11d175f60ed670027e1afdd16558919fe47023ecd38329e0b7f',
    '5578901234', '1985-09-07',
    (SELECT id FROM departamento WHERE nombre = 'Finanzas'),
    (SELECT id FROM rol WHERE nombre = 'Administrador')
),
(
    'Laura', 'Ramirez', 'Cruz',
    'laura.ramirez@ejemplo.com',
    'b460b1982188f11d175f60ed670027e1afdd16558919fe47023ecd38329e0b7f',
    '5589012345', '1997-01-25',
    (SELECT id FROM departamento WHERE nombre = 'Recursos Humanos'),
    (SELECT id FROM rol WHERE nombre = 'Analista')
),
(
    'Miguel', 'Torres', 'Diaz',
    'miguel.torres@ejemplo.com',
    'b460b1982188f11d175f60ed670027e1afdd16558919fe47023ecd38329e0b7f',
    '5590123456', '1991-07-14',
    (SELECT id FROM departamento WHERE nombre = 'Tecnologias de la Informacion'),
    (SELECT id FROM rol WHERE nombre = 'Desarrollador')
),
(
    'Elena', 'Flores', 'Morales',
    'elena.flores@ejemplo.com',
    'b460b1982188f11d175f60ed670027e1afdd16558919fe47023ecd38329e0b7f',
    '5510987654', '1996-03-30',
    (SELECT id FROM departamento WHERE nombre = 'Marketing'),
    (SELECT id FROM rol WHERE nombre = 'Usuario')
);
