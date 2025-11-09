-- 🔹 Actualizar constraint de estado_pago a valores en mayúsculas (según Enum Java)
DO $$
BEGIN
    -- Eliminar constraint antigua si existe
    IF EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE table_name = 'pagos'
        AND constraint_name = 'tipo_pagos_estado_pago_check'
    ) THEN
        ALTER TABLE pagos DROP CONSTRAINT tipo_pagos_estado_pago_check;
    END IF;

    -- Crear la nueva constraint con valores válidos (mayúsculas)
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE table_name = 'pagos'
        AND constraint_name = 'chk_estado_pago'
    ) THEN
        ALTER TABLE pagos
        ADD CONSTRAINT chk_estado_pago
        CHECK (estado_pago IN ('PENDIENTE', 'COMPLETADO', 'FALLIDO'));
    END IF;
END $$;
