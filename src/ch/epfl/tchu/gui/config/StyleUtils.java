package ch.epfl.tchu.gui.config;

import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

/**
 * Utility class for applying consistent styles to UI components.
 *
 * @author Jeremy Chaverot (315858)
 */
public final class StyleUtils {

    private static final String BASE_BORDER_COLOR = "#bdc3c7";
    private static final int BORDER_RADIUS = 5;
    private static final int PADDING = 8;

    // Private constructor to prevent instantiation
    private StyleUtils() {}

    /**
     * Applies consistent styling to a text field with focus effects.
     *
     * @param field the text field to style
     * @param focusColor the border color when the field is focused
     */
    public static void styleTextField(TextField field, String focusColor) {
        field.setPrefHeight(35);
        applyTextFieldStyle(field, BASE_BORDER_COLOR, 1);

        // Add focus listener for border color change
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                applyTextFieldStyle(field, focusColor, 2);
            } else {
                applyTextFieldStyle(field, BASE_BORDER_COLOR, 1);
            }
        });
    }

    /**
     * Applies consistent styling to a spinner.
     *
     * @param spinner the spinner to style
     */
    public static void styleSpinner(Spinner<?> spinner) {
        spinner.setPrefHeight(35);
        spinner.setStyle(
                "-fx-background-color: white; " +
                        "-fx-border-color: " + BASE_BORDER_COLOR + "; " +
                        "-fx-border-radius: " + BORDER_RADIUS + "; " +
                        "-fx-background-radius: " + BORDER_RADIUS + ";"
        );
    }

    /**
     * Applies consistent styling to a button with hover effects.
     *
     * @param button the button to style
     * @param baseColor the background color in normal state
     * @param hoverColor the background color when hovered
     */
    public static void styleButton(Button button, String baseColor, String hoverColor) {
        applyButtonStyle(button, baseColor);

        // Add hover listeners
        button.setOnMouseEntered(e -> applyButtonStyle(button, hoverColor));
        button.setOnMouseExited(e -> applyButtonStyle(button, baseColor));
    }

    /**
     * Applies the text field style with specified border color and width.
     */
    private static void applyTextFieldStyle(TextField field, String borderColor, int borderWidth) {
        field.setStyle(
                "-fx-background-color: white; " +
                        "-fx-border-color: " + borderColor + "; " +
                        "-fx-border-width: " + borderWidth + "; " +
                        "-fx-border-radius: " + BORDER_RADIUS + "; " +
                        "-fx-background-radius: " + BORDER_RADIUS + "; " +
                        "-fx-padding: " + PADDING + ";"
        );
    }

    /**
     * Applies the button style with specified background color.
     */
    private static void applyButtonStyle(Button button, String backgroundColor) {
        button.setStyle(
                "-fx-background-color: " + backgroundColor + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 8; " +
                        "-fx-cursor: hand;"
        );
    }
}