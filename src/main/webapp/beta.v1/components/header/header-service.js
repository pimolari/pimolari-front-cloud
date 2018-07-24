	angular.module('ew.header.service', [])

	
	// Service definition
	.service('HeaderService', function($http) {
		
		//var current = {id:'0', name:'Zero'};
		
		var _projects = [];
		var _currentProjectId = null;
		var _currentProject = null;
		
		/***/
		this.setProjectList = function(projects) {
			//console.log("  >> Sets project list " + projects);
			_projects = projects;
			if (_currentProject === null) {
				var currentProject = this.getCurrentProject("headerService setProjectList");
				//this.setCurrentProject(_currentProjectId);
				
				/*for (var i = 0; i < _projects.length; i++) {
					if (_projects[i].default == true) {
							this.setCurrentProject(_projects[i].id);
							break;
					}
				}
			
				if (_currentProject === null && _projects.length > 0) {
					this.setCurrentProject(_projects[0].id);
				}*/
			}
			
			return _projects;
		}	
	
		/***/
		this.getProjectList = function() {
			//console.log("    HeaderService.getProjectList() returning " + _projects.length + " projects");
			return _projects;
		}

		/***/
		this.getProject = function(projectId) {
			//console.log("  >> Get project from project list " + projectId);

			for (var i = 0; i < _projects.length; i++) {
				if (new String(_projects[i].id).valueOf() == new String(projectId).valueOf()) {
					return _projects[i];
				}
			}
			return null;
		}

		/***/
		this.setCurrentProject = function(projectId) {
			//console.log("  >> Sets current project to " + projectId);
			_currentProjectId = projectId;
			var project = this.getProject(projectId);
			//console.log("     >> Project with id " + projectId + " object: " + project);
			if (project != null) {
				//console.log("     >> Project with name " + project.name);
				_currentProject = project;
				//console.log("  >> Current project set ok to " + projectId + "named: " + project.name);
			}
			return _currentProject;
		}
		
		/***/
		this.getCurrentProject = function(trace) {
			//console.log("  >> Returns current project called from " + trace);
			if (_currentProject != null) {
				//console.log("     >> _currentProject variable " + _currentProject.id);
				return _currentProject;
			} else if (_currentProjectId != null) {
				this.setCurrentProject(_currentProjectId);
				//console.log("     >> _currentProject variable set after _currentProjectId " + _currentProjectId);
				return _currentProject;
			} else if (_projects != null && _projects.length > 0){
				var project = this.getDefaultProject();
				_currentProjectId = project.id;
				_currentProject = project;
				//console.log("     >> _currentProject variable set on default project " + _currentProject.id);
				return _currentProject;
			} else {
				//console.log("     >> null ");
				return null;
			}
			
				
		}
		/***/
		this.getDefaultProject = function() {
			
			if (_projects != null && _projects.length > 0) {
				for (var i = 0; i < _projects.length; i++) {
					if (_projects[i].default == true) {
						return _projects[i];
						break;
					}
				}
				return _projects[0];
			}
			
			return null;
		}
		
	});




		
