object TABLEHMM '($java-object (de.mmis.utilities.hmm.discrete.MSHMMFilter ("T9" "T1" "T2" "T3" "T4" "T5" "T6" "T7" "T8")))' 

addDevice de.mmis.devices.ubisense.server.Ubisense * '(addUbisenseObservationSource @ID @PROXY)'

#                                       Door   S1     S3    T3    T4    T5    T6     T7     T8 
invoke '(setPrior                      (1      0.0    0.0   0.0   0.0   0.0   0.0    0.0    0.0))'
invoke '(setTransitionProbabilities 0  (0.9    0.05   0.0   0.0   0.0   0.0   0.0    0.025  0.025))' # D
invoke '(setTransitionProbabilities 1  (0.025  0.9    0.05  0.0   0.0   0.0   0.0    0.025  0.0 ))' # S1
invoke '(setTransitionProbabilities 2  (0.0    0.05   0.9   0.05  0.0   0.0   0.0    0.0    0.0 ))' # S3
invoke '(setTransitionProbabilities 3  (0.0    0.0    0.05  0.9   0.05  0.0   0.0    0.0    0.0 ))' # 3
invoke '(setTransitionProbabilities 4  (0.0    0.0    0.0   0.05  0.9   0.05  0.0    0.0    0.0 ))' # 4
invoke '(setTransitionProbabilities 5  (0.0    0.0    0.0   0.0   0.05  0.9   0.05   0.0    0.0 ))' # 5
invoke '(setTransitionProbabilities 6  (0.025  0.0    0.0   0.0   0.0   0.05  0.9    0.025  0.0 ))' # 6
invoke '(setTransitionProbabilities 7  (0.025  0.025  0.0   0.0   0.0   0.0   0.025  0.9    0.025))' # 7
invoke '(setTransitionProbabilities 8  (0.025  0.025  0.0   0.0   0.0   0.0   0.025  0.025  0.05))' # 8

invoke '(setNormalDiagObservationModel 0 (3.50 0.50) (1.0 1.0))' # D
invoke '(setNormalDiagObservationModel 1 (0.00 2.50) (1.0 1.0))' # S1
invoke '(setNormalDiagObservationModel 2 (0.00 5.00) (1.0 1.0))' # S3
invoke '(setNormalDiagObservationModel 3 (4.40 5.80) (0.5 0.5))' # 3
invoke '(setNormalDiagObservationModel 4 (5.80 5.80) (0.5 0.5))' # 4
invoke '(setNormalDiagObservationModel 5 (7.70 4.50) (0.5 0.5))' # 5
invoke '(setNormalDiagObservationModel 6 (7.70 4.00) (0.5 0.5))' # 6
invoke '(setNormalDiagObservationModel 7 (5.80 1.70) (0.5 0.5))' # 7
invoke '(setNormalDiagObservationModel 8 (4.40 1.70) (0.5 0.5))' # 8

pubsub
