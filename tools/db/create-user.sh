#!/bin/bash

DATABASE="$1"
COLLECTION="user"
CONNECTION_STRING="mongodb://localhost:27017"

# Create the collection with the JSON schema validator

mongosh --eval "db.createCollection('${COLLECTION}', {
   validator: {
      \$jsonSchema: {
         bsonType: 'object',
         required: [
           '_id',
           'createdAt',
           'updatedAt',
           'version',
           'personal',
           'email',
           'password',
           'isHelper',
           'isLeader',
           'partner',
           'drinks',
           'countedCourses',
           'completedCourseList',
           'userGroup',
           'account'
         ],
         properties: {
           '_id': { 'bsonType': 'string' },
           'createdAt': { 'bsonType': 'date' },
           'updatedAt': { 'bsonType': 'date' },
           'version': { 'bsonType': ['long', 'null'] },
           'personal': {
             'bsonType': 'object',
             'required': ['firstName', 'lastName'],
             'properties': {
               'firstName': { 'bsonType': 'string' },
               'lastName': { 'bsonType': 'string' },
               'profilPicture': { 'bsonType': 'string' }
             },
             'additionalProperties': false
           },
           'email': {
             'bsonType': 'object',
             'required': ['email'],
             'properties': {
               'email': { 'bsonType': 'string' }
             },
             'additionalProperties': false
           },
           'password': {
             'bsonType': 'object',
             'required': ['password'],
             'properties': {
               'password': { 'bsonType': 'string' }
             },
             'additionalProperties': false
           },
           'isHelper': { 'bsonType': 'bool' },
           'isLeader': { 'bsonType': 'bool' },
           'partner': {
             'bsonType': 'object',
             'required': ['self'],
             'properties': {
               'self': { 'bsonType': 'string' },
               'partners': {
                 'bsonType': 'array',
                 'items': { 'bsonType': 'string' },
                 'uniqueItems': true
               }
             },
             'additionalProperties': false
           },
           'drinks': {
             'bsonType': 'object',
             'additionalProperties': { 'bsonType': 'int' }
           },
           'countedCourses': {
             'bsonType': 'object',
             'additionalProperties': {
               'bsonType': 'string',
               'enum': [
                 'LEVEL1',
                 'LEVEL2',
                 'LEVEL3',
                 'LEVEL4',
                 'LEVEL5',
                 'COUNTEDCOURSE',
                 'WORKSHOP',
                 'EVENT',
                 'CHECKIN'
               ]
             }
           },
         'completedCourseList': {
           'bsonType': 'array',
           'uniqueItems': true,
           'items': {
             'bsonType': 'object',
             'required': ['isLeader', 'courseID', 'userID', 'courseType'],
             'properties': {
               'isLeader': { 'bsonType': 'bool' },
               'courseID': { 'bsonType': 'string' },
               'userID': { 'bsonType': 'string' },
               'courseType': {
                 'bsonType': 'string',
                 'enum': [
                   'LEVEL1',
                   'LEVEL2',
                   'LEVEL3',
                   'LEVEL4',
                   'LEVEL5',
                   'COUNTEDCOURSE',
                   'WORKSHOP',
                   'EVENT',
                   'CHECKIN'
                 ]
               }
             },
             'additionalProperties': false
           }
         },
         'userGroup': {
           'bsonType': 'string',
           'enum': ['ADMIN', 'TEACHER', 'STUDENT']
         },
         'account': {
           'bsonType': 'object',
           'required': ['enabled'],
           'properties': {
             'enabled': { 'bsonType': 'bool' },
             'verificationToken': {
               'bsonType': ['null', 'object'],
               'properties': {
                 'email': { 'bsonType': 'string' },
                 'expiresAt': { 'bsonType': 'date' },
                 'validatorHashed': { 'bsonType': 'string' }
               },
               'additionalProperties': false
             }
           },
           'additionalProperties': false
         }
       },
       'additionalProperties': false
    }
 },
 'validationLevel': 'strict',
 'validationAction': 'error'
)" --quiet --host "${CONNECTION_STRING}" "${DATABASE}"
