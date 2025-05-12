from flask import Flask
from flask import request, jsonify
from service.messageService import MessageService
from kafka import KafkaProducer
import json

app = Flask(__name__)

messageService = MessageService()
producer = KafkaProducer(bootstrap_servers=['localhost:9092'],
                        value_serializer=lambda x: json.dumps(x).encode('utf-8'))

@app.route('/v1/ds/message', methods=['POST'])
def message():
    data = request.json.get('message')
    result = messageService.processMessage(data)
    serialized_result = result.json();

    producer.send('ds-topic', serialized_result)

    return jsonify(result)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000, debug=True)

