object wv "($java-object (de.mmis.applications.whiteboardviewer.WhiteboardViewerImpl 4))"
addDevice de.mmis.devices.camera.AXISHTTPv2 eye321-2 '(setCamera @PROXY)' Socket
addDevice de.mmis.core.gbi.GoalBasedInteractor * '(addGBI @ID @PROXY)' Socket
addDevice de.mmis.applications.presenter.ImagePerspectiveTransformation * '(setPresenter @PROXY)' Socket
subscribeC '$EventType=MAP_State_Changed' '(processHMMEvent @EVENT_Src @EVENT_MAPStateName)'
subscribeC '$EventType=PENTAKEN' '(penTaken)'
subscribeC '$EventType=BUTTON_MESSAGE && $Tag_id_string = \"010-000-004-092\"' '(reset)'


 