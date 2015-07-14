object WHITEBOARDHMM '($java-object (de.mmis.utilities.hmm.discrete.MSHMMFilter ("W1" "W2" "W3" "W4")))' 

addDevice de.mmis.devices.ubisense.server.Ubisense * '(addUbisenseObservationSource @ID @PROXY)'

#                                       W1     W2     W3    W4    
invoke '(setPrior                      (0.25   0.25   0.25  0.25))'
invoke '(setTransitionProbabilities 0  (0.9    0.08   0.0   0.02))' # W1
invoke '(setTransitionProbabilities 1  (0.08   0.9    0.02  0.0))' # W2
invoke '(setTransitionProbabilities 2  (0.0    0.02   0.9   0.08))' # W3
invoke '(setTransitionProbabilities 3  (0.02   0.0    0.08  0.9))' # W4

invoke '(setNormalDiagObservationModel 0 (0 2.49) (0.3 0.3))' # W1=^S1
invoke '(setNormalDiagObservationModel 1 (0 5.17) (0.3 0.3))' # W2=^S3
invoke '(setNormalDiagObservationModel 2 (8.65 5.17) (0.3 0.3))' # W3=^S5
invoke '(setNormalDiagObservationModel 3 (8.65 2.49) (0.3 0.3))' # W4=^S7

pubsub
