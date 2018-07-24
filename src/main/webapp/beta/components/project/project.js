//(function() {
var devices = [];
	
angular.module("ew.project")
	
.run(function(HeaderService, ProjectService) {
	// This is effectively part of the main method initialization code
	console.log("Project Module initialised");
})

.controller("ProjectController", function($scope, $rootScope, $location, HeaderService, ProjectService, DeviceService) { 
	console.log("Project Controller executing...");
			
	var _projects = HeaderService.getProjects();
	var _currentProject = HeaderService.getCurrentProject("project");
	
	/***/
	$scope.$on('currentProject_change', function() {
		//$scope.project = HeaderService.getCurrentProject("Project currentProject_change");
		console.log("  Project controller currentProject_change received and does nothing with it");
	});
	
	/***/
	$scope.saveProject = function(project) {
		
		if (project != null) {
			ProjectService.post(project)
			.then(function(data) { 
				project = data;
				console.log("  Project controller says that project was saved " + project.name + " :: " + project.id);
				HeaderService.addProject(project, true);
				$rootScope.$broadcast('projectList_change');
				$rootScope.$broadcast('currentProject_change');
				
				$scope.navigate("/");
				//HeaderService.setCurrentProject();
				/*ProjectService.list()
			  .then(function(data) { 
			
				  console.log("  * Retrieved " + data.length + " projects");
					HeaderService.setProjectList(data);
					HeaderService.setCurrentProject(project.id);

					$rootScope.project = HeaderService.getCurrentProject("project saveProject");
					$rootScope.$broadcast('ProjectListChange');
					$location.path('monitor/' + project.id);
					
					
				}).catch(function(data) {
					//console.log("  Error getting data " + data);
				});*/
				
			}).catch(function(data) {
				console.log("  Project controller says that an error ocurred when saving project " + data.name);
			});
		}
	}
	
	$scope.navigate = function(path) {
		$location.path(path);
	}
});
		
		

		

