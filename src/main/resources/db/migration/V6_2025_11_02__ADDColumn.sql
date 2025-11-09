-- ✅ Migración segura: agrega la columna y la FK solo si no existen

DO $$
BEGIN
    -- Agregar columna id_vendedor si no existe
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'pedido_detalle'
        AND column_name = 'id_vendedor'
    ) THEN
        ALTER TABLE pedido_detalle ADD COLUMN id_vendedor INTEGER;
    END IF;

    -- Agregar constraint si no existe
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE table_name = 'pedido_detalle'
        AND constraint_name = 'fk_pedido_detalle_vendedor'
    ) THEN
        ALTER TABLE pedido_detalle
        ADD CONSTRAINT fk_pedido_detalle_vendedor
        FOREIGN KEY (id_vendedor) REFERENCES usuarios(id_usuario);
    END IF;
END $$;
