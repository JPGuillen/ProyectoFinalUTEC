-- 1️⃣ ELIMINAR CONSTRAINTS ANTIGUOS (si existen)
ALTER TABLE usuarios DROP CONSTRAINT IF EXISTS usuarios_rol_check;
ALTER TABLE usuarios DROP CONSTRAINT IF EXISTS chk_tienda_por_rol;
ALTER TABLE usuarios DROP CONSTRAINT IF EXISTS chk_tienda_por_rol_v2;

-- 2️⃣ ACTUALIZAR LOS VALORES DE ROL A MAYÚSCULAS
UPDATE usuarios
SET rol = UPPER(rol);

-- 3️⃣ VOLVER A CREAR EL CHECK DE ROLES VÁLIDOS
ALTER TABLE usuarios
ADD CONSTRAINT usuarios_rol_check
CHECK (rol IN ('CLIENTE', 'VENDEDOR'));

-- 4️⃣ VOLVER A CREAR EL CHECK DE TIENDA SEGÚN ROL
-- ============================================================
ALTER TABLE usuarios
ADD CONSTRAINT chk_tienda_por_rol
CHECK (
    (rol = 'VENDEDOR' AND nombre_publico_tienda IS NOT NULL)
    OR
    (rol = 'CLIENTE' AND nombre_publico_tienda IS NULL)
);