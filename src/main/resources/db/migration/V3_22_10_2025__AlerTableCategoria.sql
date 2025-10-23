
-- Restricción adicional (opcional)
-- Evitar nombres duplicados dentro del mismo vendedor, pero no entre vendedores:


ALTER TABLE categorias
ADD CONSTRAINT unique_nombre_categoria UNIQUE (nombre);