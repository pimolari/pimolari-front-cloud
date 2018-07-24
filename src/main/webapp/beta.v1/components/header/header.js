
angular.module("ew.header")
	
.config(['$routeProvider', function($routeProvider, $locationProvider) {
	//$locationProvider.hashPrefix('!');
	$routeProvider
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
	
	var _projects = [];
	
	$scope.$on('currentProjectChange', function() {
		$rootScope.project = HeaderService.getCurrentProject("header currentProjectChange");
		$scope.project = $rootScope.project; 
		console.log("  Event currentProjectChange received in header " + $scope.project.name);
	});
	
	$scope.$on('ProjectListChange', function() {
		
		$rootScope.projects = HeaderService.getProjectList();
		$scope.projects = $rootScope.projects;
		console.log("  Event ProjectListChange received in header " + $scope.projects.length);
		//$rootScope.project = HeaderService.getCurrentProject("header ProjectListChange event");
		//$scope.project = $rootScope.project; 
	});
	
	$scope.load = function() {
		console.log("  Loading header");
		
		ProjectService.list()
	  .then(function(data) { 
			
			_projects = data;
			
			if (_projects.length == 0) {
				console.log("  No projects were retrieved... ");
			} else {
				$rootScope.projects = HeaderService.setProjectList(_projects);
				$scope.projects = $rootScope.projects;
				$rootScope.project = HeaderService.getCurrentProject("header load");
				$scope.project = $rootScope.project; 
				$rootScope.$broadcast('ProjectListChange');
				$rootScope.$broadcast('currentProjectChange');
			}
			
		}).catch(function(data) {
			console.log("  Error getting data " + data);
		});
	}
	
	if (HeaderService.getProjectList().length == 0) {
		console.log("  Project list is empty, loads projects");
		$scope.load();
		
	} 
			
});
		


