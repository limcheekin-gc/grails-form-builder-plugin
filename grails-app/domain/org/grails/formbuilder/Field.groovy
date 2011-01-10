package org.grails.formbuilder

class Field {
  String name
  String type
  String settings
  Integer sequence
  FieldStatus fieldStatus
  
  static transients = ['fieldStatus']
  
  static constraints = {
	  name nullable:false
	  type nullable:false
	  settings nullable:false
	  sequence nullable:false
    }
}

enum FieldStatus {
	NEW,
	DELETE
}
