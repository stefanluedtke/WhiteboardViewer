object wv "($java-object (de.mmis.applications.whiteboardviewer.WhiteboardViewerImpl 3))"
addDevice de.mmis.devices.camera.AXISHTTPv2 eye321-2 '(setCamera @PROXY)' Socket
addDevice de.mmis.core.gbi.GoalBasedInteractor * '(addGBI @ID @PROXY)' Socket
subscribeC '$EventType=MAP_State_Changed' '(processHMMEvent @EVENT_Src @EVENT_MAPStateName)'
subscribeC '$EventType=PENTAKEN' '(penTaken)'


 