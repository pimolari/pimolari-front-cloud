
angular.module("ew.header")
	
.config(['$routeProvider', function($routeProvider, $locationProvider) {
	//$locationProvider.hashPrefix('!');
	$routeProvider
	.when('/', {
		templateUrl: '/beta/components/monitor/monitor.html' // Project monitor
	})
	.when('/monitor/', {
		templateUrl: '/beta/components/monitor/monitor.html' // Project monitor
	})
	.when('/monitor/:projectId', {
		templateUrl: '/beta/components/monitor/monitor.html' // Project monitor
	})
	.when('/device/:deviceId', {
		templateUrl: '/beta/components/device/device-form.html' // Device details form
	})
	.when('/project', { 
		templateUrl: '/beta/components/project/project-form.html' // New project form
	})
	.when('/device', { 
		templateUrl: '/beta/components/device/device-form.html' // New project form
	})
	
 
	.otherwise({
		templateUrl: '/beta/components/monitor/monitor.html'
	});

}])

.run(function(HeaderService, ProjectService) {
	// This is effectively part of the main method initialization code
	console.log("Header Module initialised");
})

.controller("HeaderController", function($scope, $rootScope, $location, HeaderService, ProjectService) { 
	console.log("Header Controller executing... now");
	
	$scope.$on('currentProject_change', function() {
		//$rootScope.project = HeaderService.getCurrentProject("header currentProjectChange");
		//$scope.project = $rootScope.project; 
		console.log("  Header currentProject_change received ");
		$scope.project = HeaderService.getCurrentProject("header currentProject_change");
	});
	
	$scope.$on('projectList_change', function() {
		//$rootScope.projects = HeaderService.getProjectList();
		//$scope.projects = $rootScope.projects;
		console.log("  Header pojectList_change received ");
		$scope.projects = HeaderService.getProjects();
		console.log("  Header project list set to " + $scope.projects.length + " items");
		//$rootScope.project = HeaderService.getCurrentProject("header ProjectListChange event");
		//$scope.project = $rootScope.project; 
	});
	
	$scope.load = function() {
		console.log("  Loading header");
		
		ProjectService.list()
	  .then(function(data) { 
			
			if (data.length == 0) {
				console.log("  No projects were retrieved... ");
			} else {
				$scope.projects = HeaderService.setProjects(data);
				$rootScope.$broadcast('projectList_change');
				$rootScope.$broadcast('currentProject_change');
				
				//$rootScope.projects = HeaderService.setProjectList(_projects);
				//$scope.projects = $rootScope.projects;
				//$rootScope.project = HeaderService.getCurrentProject("header load");
				//$scope.project = $rootScope.project; 
			}
			
		}).catch(function(data) {
			console.log("  Error getting data " + data);
		});
	}
	
	if (HeaderService.getProjects().length == 0) {
		console.log("  Project list is empty, loads projects");
		$scope.load();
		
	} 
			
});
		


