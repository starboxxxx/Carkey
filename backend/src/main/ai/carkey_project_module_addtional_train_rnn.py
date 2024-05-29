from carkey_additional_train_rnn import ComplexRNNModel_train
import sys
import warnings

def ComplexRNNModel_trained(epochs):
    warnings.filterwarnings("ignore")
    epoch = int(epochs)
    model_dir = '/home/t24120/carkey_v1.0/backend/src/main/ai/rnn_model'
    ComplexRNNModel_train(epoch, model_dir)

ComplexRNNModel_trained(sys.argv[1])