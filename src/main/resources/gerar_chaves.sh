#!/bin/bash

# Script para gerar chaves pública e privada PEM usando OpenSSL

# Verifica se o OpenSSL está instalado
if ! command -v openssl &> /dev/null; then
    echo "Erro: OpenSSL não está instalado ou não está no PATH."
    echo "Por favor, instale o OpenSSL e tente novamente."
    exit 1
fi

# Nomes dos arquivos de saída
PRIVATE_KEY="privateKey.pem"
PUBLIC_KEY="publicKey.pem"

# Verifica se os arquivos já existem para evitar sobrescrita
if [ -f "$PRIVATE_KEY" ] || [ -f "$PUBLIC_KEY" ]; then
    echo "Erro: Um ou ambos os arquivos ($PRIVATE_KEY ou $PUBLIC_KEY) já existem."
    echo "Por favor, remova-os ou renomeie-os antes de executar este script."
    exit 1
fi

# Gera a chave privada RSA de 2048 bits
echo "Gerando chave privada..."
openssl genpkey -algorithm RSA -out "$PRIVATE_KEY" -pkeyopt rsa_keygen_bits:2048

if [ $? -ne 0 ]; then
    echo "Erro ao gerar a chave privada."
    exit 1
fi

# Gera a chave pública a partir da chave privada
echo "Gerando chave pública..."
openssl rsa -pubout -in "$PRIVATE_KEY" -out "$PUBLIC_KEY"

if [ $? -ne 0 ]; then
    echo "Erro ao gerar a chave pública."
    exit 1
fi

# Ajusta as permissões da chave privada (opcional, mas recomendado)
chmod 400 "$PRIVATE_KEY"

echo "Chaves geradas com sucesso:"
echo "- Chave privada: $PRIVATE_KEY"
echo "- Chave pública: $PUBLIC_KEY"