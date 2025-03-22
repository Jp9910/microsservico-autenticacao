#!/bin/bash

# Com as configurações atuais, o client faz uma requisição por segundo,
# Para aumentar o número de requisições por segundo, descomente as linhas
# 14 e 32 e comente a linha 33, você terá que remover tanto a imagem como
# o container client-forum-api e subir a stack novamente para o rebuild.

HOST='api-autenticacao:8081'
# ARQUIVOS SH DEVEM TER `LF` COMO FIM DE LINHA
# curl --data '{"email":"admin@email.com","senha":"rootadmin"}' --header "Content-Type:application/json" --request POST http://api-autenticacao:8081/auth/login
# token=$(curl --data '{"email":"admin@email.com","senha":"rootadmin"}' --header "Content-Type:application/json" --request POST http://api-autenticacao:8081/auth/login | jq -r '.token')

while true
    do
	ENDP=`expr $RANDOM % 3 + 1`
	NUMB=`expr $RANDOM % 100 + 1`
	#TEMP=`expr 1 + $(awk -v seed="$RANDOM" 'BEGIN { srand(seed); printf("%.4f\n", rand()) }')`

	# se for <=55, faz um GET para rota acessível por qualquer um
	if [ $NUMB -le 55 ]; then
		echo 1
	    curl --output /dev/null http://${HOST}/actuator # --output /dev/null para não imprimir o retorno no console

	# se for >=56 e <=85, faz um GET para uma rota só acessível por admins
    elif [ $NUMB -ge 56 ] && [ $NUMB -le 85 ] ; then
		echo 2
	    curl --header "Authorization:$token" http://${HOST}/usuarios

	# se for >=86 e <=95, faz uma autenticação de admin com sucesso e salva o token na variavel $token
    elif [ $NUMB -ge 86 ] && [ $NUMB -le 95 ] ; then
		echo 3
		token=$(curl --data '{"email":"admin@email.com","senha":"rootadmin"}' \
		 --header "Content-Type:application/json" \
		 --request POST http://${HOST}/auth/login | jq -r '.token')
		echo "token salvo: $token"

	# entre 96 e 98, autenticação falha e reseta o token
    elif [ $NUMB -ge 96 ] && [ $NUMB -le 98 ] ; then
	    echo 4
		curl --data '{"email":"admin@email.com","senha":"senhaIncorreta"}' \
	         --header "Content-Type:application/json" \
	         --request POST http://${HOST}/auth
		token=""

	# 99 ou 100, faz um GET pra uma rota que não existe (vai retornar um erro 500, mas deveria ser um 404)
	else
		echo 5
	    curl http://${HOST}/rotaNaoExiste
	fi

	#sleep $TEMP
	sleep 0.75
done
