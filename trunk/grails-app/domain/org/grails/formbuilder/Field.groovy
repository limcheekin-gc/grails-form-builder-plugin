package org.grails.formbuilder

import java.util.Date;

class Field {
  String name
  String type
  String settings
  Integer sequence
  FieldStatus status
  Date dateCreated
  Date lastUpdated
  
  static transients = ['status']
  
  static constraints = {
	  name nullable:false
	  type nullable:false
	  settings nullable:false
	  sequence nullable:false
	  dateCreated blank:false
	  lastUpdated nullable:true
    }
}

enum FieldStatus {
	U, // UPDATED
	D // DELETED
}
