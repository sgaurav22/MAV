package com.mav.module.student

class StudentRegistrationController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [studentRegistrationInstanceList: StudentRegistration.list(params), studentRegistrationInstanceTotal: StudentRegistration.count()]
    }

    def create = {
        def studentRegistrationInstance = new StudentRegistration()
        studentRegistrationInstance.properties = params
        return [studentRegistrationInstance: studentRegistrationInstance]
    }

    def save = {
        def studentRegistrationInstance = new StudentRegistration(params)
        if (studentRegistrationInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'studentRegistration.label', default: 'StudentRegistration'), studentRegistrationInstance.id])}"
            redirect(action: "show", id: studentRegistrationInstance.id)
        }
        else {
            render(view: "create", model: [studentRegistrationInstance: studentRegistrationInstance])
        }
    }

    def show = {
        def studentRegistrationInstance = StudentRegistration.get(params.id)
        if (!studentRegistrationInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studentRegistration.label', default: 'StudentRegistration'), params.id])}"
            redirect(action: "list")
        }
        else {
            [studentRegistrationInstance: studentRegistrationInstance]
        }
    }

    def edit = {
        def studentRegistrationInstance = StudentRegistration.get(params.id)
        if (!studentRegistrationInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studentRegistration.label', default: 'StudentRegistration'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [studentRegistrationInstance: studentRegistrationInstance]
        }
    }

    def update = {
        def studentRegistrationInstance = StudentRegistration.get(params.id)
        if (studentRegistrationInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (studentRegistrationInstance.version > version) {
                    
                    studentRegistrationInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'studentRegistration.label', default: 'StudentRegistration')] as Object[], "Another user has updated this StudentRegistration while you were editing")
                    render(view: "edit", model: [studentRegistrationInstance: studentRegistrationInstance])
                    return
                }
            }
            studentRegistrationInstance.properties = params
            if (!studentRegistrationInstance.hasErrors() && studentRegistrationInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'studentRegistration.label', default: 'StudentRegistration'), studentRegistrationInstance.id])}"
                redirect(action: "show", id: studentRegistrationInstance.id)
            }
            else {
                render(view: "edit", model: [studentRegistrationInstance: studentRegistrationInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studentRegistration.label', default: 'StudentRegistration'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def studentRegistrationInstance = StudentRegistration.get(params.id)
        if (studentRegistrationInstance) {
            try {
                studentRegistrationInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'studentRegistration.label', default: 'StudentRegistration'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'studentRegistration.label', default: 'StudentRegistration'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'studentRegistration.label', default: 'StudentRegistration'), params.id])}"
            redirect(action: "list")
        }
    }
}
