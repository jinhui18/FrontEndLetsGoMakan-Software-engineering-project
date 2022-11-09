package com.example.application.backend.entity;

import com.example.application.backend.enums.PreferredModeOfTransport;
import com.example.application.backend.enums.TypesOfDietaryRequirements;

public class Profile {
    /**
     * A list of chosen dietary requirements of the user.
     * See {@link TypesOfDietaryRequirements} for the different types of dietary requirements.
     */
    private TypesOfDietaryRequirements diet;


    private float maxTravel;

    /**
     * The mode of transport the user prefers.
     * See {@link PreferredModeOfTransport} for the different modes of transport.
     */
    private PreferredModeOfTransport transportMode;

    public Profile() {
        this.diet = diet;
        this.maxTravel = maxTravel;
        this.transportMode = transportMode;
    }
    /**
     * Class constructor.
     * Creates a new profile for the user.
     * @param diet                This user's dietary requirements
     * @param maxTravel            This user's preferred maximum travelling time
     * @param transportMode        This user's preferred mode of transport
     */
    public Profile(TypesOfDietaryRequirements diet, float maxTravel, PreferredModeOfTransport transportMode) {
        this.diet = diet;
        this.maxTravel = maxTravel;
        this.transportMode = transportMode;
    }

    /**
     * Gets the list of dietary requirements of the user.
     * @return	this user's list of dietary requirements
     */
    public TypesOfDietaryRequirements getDietaryRequirements() {
        return diet;
    }

    /**
     * Sets the list of dietary requirements of the user.
     * @param diet	This user's list of dietary requirements
     */
    public void setDietaryRequirements(TypesOfDietaryRequirements diet) {
        this.diet = diet;
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
