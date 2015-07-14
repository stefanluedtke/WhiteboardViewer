object TABLEHMM '($java-object (de.mmis.utilities.hmm.discrete.MSHMMFilter ("T1" "T2" "T3" "T4" "T5" "T6" "T7" "T8" "T9")))' 

addDevice de.mmis.devices.ubisense.server.Ubisense * '(addUbisenseObservationSource @ID @PROXY)'

#                                       T1        T2      T3     T4     T5     T6     T7      T8      T9
invoke '(setPrior                      (0.12      0.11    0.11   0.11   0.11   0.11   0.11    0.11    0.11))'
invoke '(setTransitionProbabilities 0  (0.98      0.01    0.0    0.0    0.0    0.0    0.0     0.0     0.01))' # T1
invoke '(setTransitionProbabilities 1  (0.01      0.98    0.01   0.0    0.0    0.0    0.0     0.0     0.0))' # T2
invoke '(setTransitionProbabilities 2  (0.00      0.01    0.98   0.01   0.0    0.0    0.0     0.0     0.0))' # T3
invoke '(setTransitionProbabilities 3  (0.00      0.00    0.01   0.98   0.01   0.0    0.0     0.0     0.0))' # T4
invoke '(setTransitionProbabilities 4  (0.00      0.00    0.0    0.01   0.98   0.01   0.0     0.0     0.0))' # T5
invoke '(setTransitionProbabilities 5  (0.00      0.00    0.0    0.0    0.01   0.98   0.01    0.0     0.00))' # T6
invoke '(setTransitionProbabilities 6  (0.00      0.00    0.0    0.0    0.0    0.01   0.98    0.01    0.00))' # T7
invoke '(setTransitionProbabilities 7  (0.00      0.00    0.0    0.0    0.0    0.0    0.01    0.98    0.01))' # T8
invoke '(setTransitionProbabilities 8  (0.01      0.00    0.0    0.0    0.0    0.0    0.0     0.01     0.98))' # T9

invoke '(setNormalDiagObservationModel 0 (2.67 3.90) (0.5 0.5))' # T1
invoke '(setNormalDiagObservationModel 1 (3 5) (0.5 0.5))' # T2
invoke '(setNormalDiagObservationModel 2 (4.40 5) (0.5 0.5))' # T3
invoke '(setNormalDiagObservationModel 3 (5.80 5) (0.5 0.5))' # T4
invoke '(setNormalDiagObservationModel 4 (6.95 4.67) (0.5 0.5))' # T5
invoke '(setNormalDiagObservationModel 5 (6.95 3.27) (0.5 0.5))' # T6
invoke '(setNormalDiagObservationModel 6 (5.80 2.80) (0.5 0.5))' # T7
invoke '(setNormalDiagObservationModel 7 (4.40 2.80) (0.5 0.5))' # T8
invoke '(setNormalDiagObservationModel 8 (3 2.80) (0.5 0.5))' # T9

pubsub
