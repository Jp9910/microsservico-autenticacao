# Microsserviço de autenticação

## Iniciar o serviço com Docker
Iniciar o serviço em desenvolvimento em containers (inicia todos os serviços necessários):
1. Criar arquivos de ambiente 
- `.senhapostgres.txt`;
- `.env`, de acordo com o .env.example

2. (Opcional) Configurar o perfil ativo da aplicação no application.yaml ou passando a variável de ambiente SPRING_ACTIVE_PROFILE (no container ou localmente)

3. Executar `docker-compose up --build`
