#!/bin/bash

# Com as configurações atuais, o client faz uma requisição por segundo,
# Para aumentar o número de requisições por segundo, descomente as linhas
# 14 e 32 e comente a linha 33, você terá que remover tanto a imagem como
# o container client-forum-api e subir a stack novamente para o rebuild.

HOST='api-autenticacao:8081'

while true
    do
	ENDP=`expr $RANDOM % 3 + 1`
	NUMB=`expr $RANDOM % 100 + 1`
	#TEMP=`expr 1 + $(awk -v seed="$RANDOM" 'BEGIN { srand(seed); printf("%.4f\n", rand()) }')`

	# se for <=55, faz um GET para /topicos
	if [ $NUMB -le 55 ]; then
		echo 1
	    curl --output /dev/null http://${HOST}/actuator

	# se for >=56 e <=85, faz um POST para /auth/login com credenciais de usuário comum
    elif [ $NUMB -ge 56 ] && [ $NUMB -le 85 ] ; then
		echo 2
	    curl --output /dev/null --data '{"email":"12@teste.com","senha":"senha123"}' \
		 --header "Content-Type:application/json" \
		 --request POST http://${HOST}/auth/login

	# se for >=86 e <=95, faz uma autenticação de admin com sucesso
    elif [ $NUMB -ge 86 ] && [ $NUMB -le 95 ] ; then
		echo 3
		curl --output /dev/null --data '{"email":"admin@email.com","senha":"rootadmin"}' \
		 --header "Content-Type:application/json" \
		 --request POST http://${HOST}/auth/login

	# entre 96 e 98, autenticação falha
    elif [ $NUMB -ge 96 ] && [ $NUMB -le 98 ] ; then
	    echo 4
		curl --output /dev/null --data '{"email":"admin@email.com","senha":"senhaIncorreta"}' \
	         --header "Content-Type:application/json" \
	         --request POST http://${HOST}/auth

	# 99 ou 100, faz um GET pra uma rota que não existe (vai retornar um erro 500, mas deveria ser um 404)
	else
		echo 5
	    curl --output /dev/null http://${HOST}/rotaNaoExiste
	fi

	#sleep $TEMP
	sleep 0.75
done

