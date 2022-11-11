package com.example.application.backend.entity;

import com.example.application.backend.enums.PreferredModeOfTransport;
import com.example.application.backend.enums.TypesOfDietaryRequirements;

/**
 * Profile is the entity class storing all profile information of the user.
 * It stores the maximum travel time the user is comfortabel with,
 * and the user's preferred mode of transportation
 * @author celest
 * @version 1.0
 * @since 2022-10-07
 */
public class Profile {

    /**
     * This refers to the maximum travelling time the user is comfortable with
     * This will be used to filter the recommended restaurant list by default (filter by maximum travel time)
     */
    private float maxTravel;

    /**
     * The mode of transport the user prefers.
     * See {@link PreferredModeOfTransport} for the different modes of transport.
     */
    private PreferredModeOfTransport transportMode;

    public Profile() {
        this.maxTravel = maxTravel;
        this.transportMode = transportMode;
    }
    /**
     * Class constructor.
     * Creates a new profile for the user.
     * @param maxTravel            This user's preferred maximum travelling time
     * @param transportMode        This user's preferred mode of transport
     */
    public Profile( float maxTravel, PreferredModeOfTransport transportMode) {
        this.maxTravel = maxTravel;
        this.transportMode = transportMode;
    }

    /**
     * Gets the preferred maximum travel time of the user.
     * @return	this user's preferred maximum travel time
     */
    public float getPreferredMaximumTravelTime() {
        return maxTravel;
    }

    /**
     * Sets the preferred maximum travel time of the user.
     * @param maxTravel	This user's preferred maximum travel time
     */
    public void setPreferredMaximumTravelTime(float maxTravel) {
        this.maxTravel = maxTravel;
    }

    /**
     * Gets the preferred mode of transport of the user.
     * @return	this user's preferred mode of transport
     */
    public PreferredModeOfTransport getPreferredModeOfTransport() {
        return transportMode;
    }

    /**
     * Sets the preferred mode of transport of the user.
     * @param transportMode	This user's preferred mode of transport
     */
    public void setPreferredModeOfTransport(PreferredModeOfTransport transportMode){
        this.transportMode = transportMode;
    }



}
