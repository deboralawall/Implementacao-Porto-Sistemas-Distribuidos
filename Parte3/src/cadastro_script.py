#!/usr/bin/env python3
import pika
import json
import random
import time

# Configuração da infraestrutura do LabP2D
import os
import sys

HOST_RABBIT = os.environ.get('RABBIT_HOST', None) or (sys.argv[1] if len(sys.argv) > 1 else 'localhost')
EXCHANGE_NAME = 'porto_exchange'

def main():
    try:
        # Estabelece conexão com o broker do laboratório
        connection = pika.BlockingConnection(pika.ConnectionParameters(host=HOST_RABBIT))
        channel = connection.channel()

        # Declara a Exchange do tipo Direct
        channel.exchange_declare(exchange=EXCHANGE_NAME, exchange_type='direct')

        # Cria e vincula as 3 filas exigidas pelo requisito
        filas_config = {
            'fila_navio': 'rota_navio',
            'fila_carga': 'rota_carga',
            'fila_embarque': 'rota_embarque'
        }

        for fila, rota in filas_config.items():
            channel.queue_declare(queue=fila, durable=True)
            channel.queue_bind(exchange=EXCHANGE_NAME, queue=fila, routing_key=rota)

        print(f"[*] Conectado a {HOST_RABBIT}. Filas prontas para receber requisições.")

        # --- SIMULAÇÃO DE CADASTROS ---

        # 1. Cadastrando Navios
        navios_teste = [
            {"descricao": "Navio Alfa", "capacidade": 120},
            {"descricao": "Navio Beta", "capacidade": 50},
            {"descricao": "Navio Gama", "capacidade": 200}
        ]
        for navio in navios_teste:
            corpo = json.dumps(navio)
            channel.basic_publish(
                exchange=EXCHANGE_NAME,
                routing_key='rota_navio',
                body=corpo,
                properties=pika.BasicProperties(delivery_mode=2) # Mensagem persistente
            )
            print(f"[x] Enviado para fila_navio: {corpo}")
            time.sleep(0.2)

        # 2. Cadastrando Cargas
        for i in range(1, 6):
            carga = {"descricao": f"Container-{i}", "volume": random.randint(15, 60)}
            corpo = json.dumps(carga)
            channel.basic_publish(
                exchange=EXCHANGE_NAME,
                routing_key='rota_carga',
                body=corpo,
                properties=pika.BasicProperties(delivery_mode=2)
            )
            print(f"[x] Enviado para fila_carga: {corpo}")
            time.sleep(0.1)

        # 3. Solicitando uma ordem de Embarque
        ordem_embarque = {"descricao": "Navio Alfa"}
        corpo = json.dumps(ordem_embarque)
        channel.basic_publish(
            exchange=EXCHANGE_NAME,
            routing_key='rota_add_embarque', # Rota mapeada para a fila_embarque
            body=corpo,
            properties=pika.BasicProperties(delivery_mode=2)
        )
        # Vincula uma rota extra caso queira flexibilizar
        channel.queue_bind(exchange=EXCHANGE_NAME, queue='fila_embarque', routing_key='rota_add_embarque')
        print(f"[x] Ordem de embarque enviada para fila_embarque: {corpo}")

        connection.close()
        print("[+] Todos os cadastros foram postados com sucesso!")

    except Exception as e:
        print(f"[-] Erro ao conectar ou enviar dados para {HOST_RABBIT}: {e}")

if __name__ == '__main__':
    main()