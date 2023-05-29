from flask import Flask, request
import numpy as np
import requests
from multiprocessing import Pool
import time

app = Flask(__name__)

n = 70

matrix1 = np.random.rand(n, n)
matrix2 = np.random.rand(n, n)

def multiply_chunk(chunk):
    return np.dot(chunk, matrix2)

@app.route('/multiply', methods=['POST'])
def matrix_multiply():

    start_time = time.time()

    chunk_size = 1
    chunks = [matrix1[i:i+chunk_size] for i in range(0, matrix1.shape[0], chunk_size)]

    with Pool() as pool:
        results = pool.map(multiply_chunk, chunks)

    result = np.concatenate(results)

    end_time = time.time()
    elapsed_time = (end_time - start_time) * 1000  # Перетворення у мілісекунди
    print("TIME: {} ms".format(elapsed_time))

    return {'Result': result.tolist()}

if __name__ == '__main__':
    app.run()
