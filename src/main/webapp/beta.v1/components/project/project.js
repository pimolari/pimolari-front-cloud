//(function() {
var devices = [];
	
angular.module("ew.project")
	
.run(function(HeaderService, ProjectService) {
	// This is effectively part of the main method initialization code
	console.log("Project Module initialised");
})

.controller("ProjectController", function($scope, $rootScope, $location, HeaderService, ProjectService, DeviceService) { 
	console.log("Project Controller executing...");
			
	var _projects = HeaderService.getProjectList();
	var _currentProject = HeaderService.getCurrentProject("project --");
	
	/***/
	$scope.$on('currentProjectChange', function() {
		console.log("  Event currentProjectChange received " + HeaderService.getCurrentProject("project currentProjectChange event").name + " MUST SEE IF SOMETHING IS TO BE DONE HERE");
	});
	
	/***/
	$scope.saveProject = function(project) {
		
		if (project != null) {
			ProjectService.post(project)
			.then(function(data) { 
				project = data;
				console.log("  * Project saved " + project.name + " :: " + project.id);
				
				ProjectService.list()
			  .then(function(data) { 
			
				  console.log("  * Retrieved " + data.length + " projects");
					HeaderService.setProjectList(data);
					HeaderService.setCurrentProject(project.id);

					$rootScope.project = HeaderService.getCurrentProject("project saveProject");
					$rootScope.$broadcast('ProjectListChange');
					$location.path('monitor/' + project.id);
					
					
				}).catch(function(data) {
					//console.log("  Error getting data " + data);
				});
				
			}).catch(function(data) {
				//console.log("  Error saving project " + data.name);
			});
		}
	}
	
	$scope.navigate = function(path) {
		$location.path(path);
	}
});
		
		

		

