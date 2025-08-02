ALTER TABLE usuario
ALTER COLUMN tipo_usuario_id DROP NOT NULL;

ALTER TABLE usuario
DROP CONSTRAINT IF EXISTS usuario_tipo_usuario_id_fkey;

ALTER TABLE usuario
ADD CONSTRAINT usuario_tipo_usuario_id_fkey
FOREIGN KEY (tipo_usuario_id)
REFERENCES tipo_usuario(id)
ON DELETE SET NULL;