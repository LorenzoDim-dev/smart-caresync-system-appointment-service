package com.smart_caresync_system.appointment_service.controller;

import com.smart_caresync_system.appointment_service.constant.RestApiPath;
import com.smart_caresync_system.appointment_service.dto.request.AppointmentRequest;
import com.smart_caresync_system.appointment_service.dto.response.AppointmentResponse;
import com.smart_caresync_system.appointment_service.exception.ErrorResponse;
import com.smart_caresync_system.appointment_service.model.Appointment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Appointment Controller", description = "APIs for managing appointments")
@RequestMapping(RestApiPath.BASE_APPOINTMENT)
public interface AppointmentController {

    @Operation(summary = "Create a new appointment",
                description = "Creates a new appointment.\n" +
                        "Patient and doctor must exist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment created successfully"),
            @ApiResponse(responseCode = "409", description = "Conflict during creation of appointment",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid Request")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE) //TODO sistemare eccezione se non esiste un paziente
    ResponseEntity<AppointmentResponse> scheduleAppointment(@Valid @RequestBody AppointmentRequest request);



    @Operation(summary = "Retrieve all appointments for a doctor")
    @ApiResponse(responseCode = "404", description = "appointment not found")
    @GetMapping(RestApiPath.DOCTOR_APPOINTMENTS)
    ResponseEntity<List<AppointmentResponse>> retrieveAppointmentsOfDoctor(@PathVariable Long doctorId);


    @Operation(summary = "Retrieve all appointments for a patient")
    @ApiResponse(responseCode = "404", description = "appointment not found")
    @GetMapping(RestApiPath.PATIENT_APPOINTMENTS)
    ResponseEntity<List<AppointmentResponse>> retrieveAppointmentsOfPatient(@PathVariable Long patientId);


    @Operation(summary = "API for doctors to approve appointments",
                description = "only PENDING requests can be approved!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "appointment approved successfully"),
            @ApiResponse(responseCode = "409", description = "Conflict during the operation,only PENDING requests can be approved",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "appointment not found")
    })
    @PutMapping(RestApiPath.APPROVE_APPOINTMENT)
    ResponseEntity<Void> approveAppointment(@PathVariable Long appointmentId);


    @Operation(summary = "API for doctors to reject appointments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "appointment rejected successfully"),
            @ApiResponse(responseCode = "409", description = "Appointment already cancelled",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "appointment not found")
    })
    @PutMapping(RestApiPath.REJECT_APPOINTMENT)
    ResponseEntity<Void> rejectAppointment(@PathVariable Long appointmentId);


    @Operation(summary = "API for doctors to mark as visited appointments",
            description = "only APPROVED requests can be mark as visited!")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "appointment updated successfully"),
            @ApiResponse(responseCode = "409", description = "Conflict during the operation,only APPROVED requests can be marked as visited",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "appointment not found")
    })
    @PutMapping(RestApiPath.MARK_AS_VISITED_APPOINTMENT)
    ResponseEntity<Void> markAsVisitedAppointment(@PathVariable Long appointmentId);

}
