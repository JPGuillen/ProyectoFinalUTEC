-- 1️⃣ Renombrar tabla si existe
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.tables
               WHERE table_name = 'tipo_pagos') THEN
        ALTER TABLE tipo_pagos RENAME TO pagos;
    END IF;
END $$;

-- 2️⃣ Agregar columna telefono_contacto si no existe
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.columns
                   WHERE table_name = 'pagos' AND column_name = 'telefono_contacto') THEN
        ALTER TABLE pagos
        ADD COLUMN telefono_contacto VARCHAR(9);
    END IF;
END $$;

-- 3️⃣ Agregar constraint para metodo si no existe
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints
                   WHERE table_name = 'pagos' AND constraint_name = 'chk_metodo_pago') THEN
        ALTER TABLE pagos
        ADD CONSTRAINT chk_metodo_pago
        CHECK (LOWER(metodo) IN ('tarjeta','yape','deposito','contraentrega'));
    END IF;
END $$;

-- 4️⃣ Agregar constraint para telefono_contacto numérico si no existe
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM information_schema.table_constraints
                   WHERE table_name = 'pagos' AND constraint_name = 'chk_telefono_contacto_numerico') THEN
        ALTER TABLE pagos
        ADD CONSTRAINT chk_telefono_contacto_numerico
        CHECK (telefono_contacto ~ '^[0-9]{9}$');
    END IF;
END $$;
