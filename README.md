# receitas-app

Este projeto utiliza Quarkus, o framework Java Subatômico Supersônico.

Se você quiser saber mais sobre o Quarkus, visite o site: <https://quarkus.io/>.

## Como iniciar a aplicação em modo de desenvolvimento

Siga os passos abaixo para configurar e executar a aplicação em modo de desenvolvimento:

### 1. Gerar Chaves de Segurança

Primeiro, você precisa gerar as chaves de segurança necessárias para a aplicação. Abra um terminal na raiz do projeto e execute o seguinte script:

```bash
./src/main/resources/gerar_chaves.sh
```

### 2. Iniciar o Backend (Quarkus)

Após gerar as chaves, abra um **novo terminal** na raiz do projeto e inicie o backend Quarkus em modo de desenvolvimento com o seguinte comando:

```bash
./mvnw quarkus:dev
```

> **NOTA:** O Quarkus agora vem com uma Dev UI, que está disponível apenas no modo de desenvolvimento em <http://localhost:8081/q/dev/>.

### 3. Iniciar o Frontend (Angular)

Enquanto o backend está sendo iniciado, abra um **terceiro terminal** e navegue até a pasta `frontend` do projeto:

```bash
cd frontend
```

Em seguida, inicie o servidor de desenvolvimento do Angular:

```bash
ng serve
```

Isso iniciará o frontend, que geralmente estará disponível em `http://localhost:4200`.

Agora você deve ter o backend e o frontend da aplicação rodando em modo de desenvolvimento.