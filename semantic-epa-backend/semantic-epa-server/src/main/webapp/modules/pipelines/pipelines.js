angular
    .module('streamPipesApp')
    .controller('PipelineCtrl', [ '$scope','restApi','$http','$rootScope','$mdDialog','$location', function ($scope, restApi, $http, $rootScope, $mdDialog, $location) {
        $scope.pipeline = {};
        $scope.pipelines = [];
        $scope.pipelinShowing = false;
        var pipelinePlumb = jsPlumb.getInstance({Container: "pipelineDisplay"});


        $scope.getPipelines = function(){
            restApi.getOwnPipelines()
                .success(function(pipelines){
                    $scope.pipelines = pipelines;
                    console.log($scope.pipelines);
                })
                .error(function(msg){
                    console.log(msg);
                });

        };
        $scope.getPipelines();


        $scope.startPipeline = function(pipelineId) {
        	restApi.startPipeline(pipelineId).success(function(data) {
        		console.log("starting pipeline");
        		$scope.getPipelines();
        	});
        };
        
        $scope.stopPipeline = function(pipelineId) {
        	restApi.stopPipeline(pipelineId).success(function(data) {
        		console.log("stopping pipeline");
        		$scope.getPipelines();
        	});
        };

        $scope.deletePipeline = function(pipelineId) {
            restApi.deleteOwnPipeline(pipelineId)
                .success(function(data){
                    console.log(data);
                })
                .error(function(data){
                    console.log(data);
                })
        };

        $scope.showPipeline = function(pipeline){
            //$mdDialog.show({
            //    controller: PipelineDialogController,
            //    locals: {pipeline: pipeline},
            //    templateUrl: 'modules/pipelines/templates/pipelineDialog.tmpl.html',
            //    parent: angular.element(document.body),
            //    clickOutsideToClose: true
            //
            //});
            clearPipelineDisplay();
            displayPipeline(pipeline);
        };
        $scope.modifyPipeline = function(pipeline){
            showPipelineInEditor(pipeline);

        };

        function displayPipeline(json){

            console.log("displayPipeline()");
            for (var i = 0, stream; stream = json.streams[i]; i++){
                createPreviewElement("stream", stream, i, json);
            }
            for (var i = 0, sepa; sepa = json.sepas[i]; i++){

                createPreviewElement("sepa", sepa, i, json);
            }
            createPreviewElement("action", json.action);
            connectPipelineElements(json, false);
            pipelinePlumb.repaintEverything(true);
        }
        function createPreviewElement(type, element, i, json){

            var $state = $("<span>")
                .addClass("connectable a")
                .attr("id", element.DOM)
                .data("JSON", $.extend(true, {}, element));
            if (element.iconUrl == null){ //Kein icon in JSON angegeben
                addTextIconToElement($state, $state.data("JSON").name);

                //.data("JSON", $.extend(true, {},element));
            }else{
                $('<img>')
                    .addClass('connectable-img tt')
                    .attr(
                    {
                        src : element.iconUrl,
                        "data-toggle": "tooltip",
                        "data-placement": "top",
                        "data-delay": '{"show": 1000, "hide": 100}',
                        title: element.name
                    })
                    .error(function(){
                        addTextIconToElement($state, $state.data("JSON").name);
                        $(this).remove();
                    })
                    .appendTo($state)

                    .data("JSON", $.extend(true, {},element));
            }

            var topLeftY, topLeftX;

            switch (type){

                case "stream":
                    $state.appendTo("body");
                    $state.addClass("stream");
                    topLeftY = getYPosition(json.streams.length , i, $("#streamDisplay"), $state);
                    topLeftX = getXPosition($("#streamDisplay"), $state);
                    $state.appendTo("#streamDisplay");
                    break;

                // jsPlumb.addEndpoint($icon,streamEndpointOptions);
                case "sepa":
                    $state.appendTo("body");
                    $state.addClass("sepa");
                    topLeftY = getYPosition(json.sepas.length , i, $("#sepaDisplay"), $state);
                    topLeftX = getXPosition($("#sepaDisplay"), $state);
                    $state.appendTo("#sepaDisplay");
                    break;

                case "action":
                    $state.appendTo("body");
                    $state.addClass("action");
                    topLeftY = $("#actionDisplay").height() / 2 - (1/2) * $state.outerHeight();
                    topLeftX = $("#actionDisplay").width() / 2 - (1/2) * $state.outerWidth();
                    $state.appendTo("#actionDisplay");
                    break;
            }
            $state.css(
                {
                    "position" : "absolute",
                    "top": topLeftY,
                    "left": topLeftX
                }
            );
        }

        function connectPipelineElements(json, detachable){
            console.log("connectPipelineElements()");
            var source, target;

            pipelinePlumb.setSuspendDrawing(true);
            if (!$.isEmptyObject(json.action)) {
                //Action --> Sepas----------------------//
                target = json.action.DOM;

                for (var i = 0, connection; connection = json.action.connectedTo[i]; i++) {
                    source = connection;

                    var sourceEndpoint = pipelinePlumb.addEndpoint(source, sepaEndpointOptions);
                    var targetEndpoint = pipelinePlumb.addEndpoint(target, leftTargetPointOptions);
                    pipelinePlumb.connect({source: sourceEndpoint, target: targetEndpoint, detachable: detachable});
                }
            }
            //Sepas --> Streams / Sepas --> Sepas---------------------//
            for (var i = 0, sepa; sepa = json.sepas[i]; i++){
                for (var j = 0, connection; connection = sepa.connectedTo[j]; j++){

                    source = connection;
                    target = sepa.DOM;


                    var options;
                    var id = "#" + source;
                    console.log($(id));
                    if ($(id).hasClass("sepa")){
                        options = sepaEndpointOptions;
                    }else{
                        options = streamEndpointOptions;
                    }

                    var sourceEndpoint = pipelinePlumb.addEndpoint(source, options);
                    var targetEndpoint = pipelinePlumb.addEndpoint(target, leftTargetPointOptions);
                    pipelinePlumb.connect({source: sourceEndpoint, target: targetEndpoint, detachable:detachable});
                }
            }
            pipelinePlumb.setSuspendDrawing(false ,true);



        }

        function clearPipelineDisplay(){
            pipelinePlumb.deleteEveryEndpoint();
            $("#pipelineDisplay").children().each(function(){
                $(this).children().remove();
            });
        }

        function showPipelineInEditor(id){

            $location.path("/editor/" + id);
        }

        function PipelineDialogController($scope, $mdDialog, pipeline){
            $scope.pipeline = pipeline;

            $scope.hide = function(){
                $mdDialog.hide();
            };
            $timeout(displayPipeline($scope.pipeline));
        }

        //$(refreshPipelines());

        //Bind click handler--------------------------------
        //$("#pipelineTableBody").on("click", "tr", function () {
        //    if (!$(this).data("active") || $(this).data("active") == undefined) {
        //        $(this).data("active", true);
        //        $(this).addClass("info");
        //        $("#pipelineTableBody").children().not(this).removeClass("info");
        //        $("#pipelineTableBody").children().not(this).data("active", false);
        //        clearPipelineDisplay();
        //        displayPipeline($(this).data("JSON"));
        //    } else {
        //
        //    }
        //});
    }]);