#!/bin/bash

# agent-tools.sh - ADB-based configuration tool for UI validation agentic workflows

function show_help() {
    local cmd=$1
    if [ -z "$cmd" ]; then
        echo "Usage: $0 <subcommand> [options]"
        echo ""
        echo "Available subcommands:"
        echo "  theme        Change UI theme (light/dark/auto)"
        echo "  density      Change screen density (DPI)"
        echo "  size         Change screen resolution (WIDTHxHEIGHT)"
        echo "  rotation     Change screen orientation"
        echo "  locale       Change device locale/language"
        echo "  font-size    Change font scale"
        echo "  navigation   Change system navigation style (gestures/button3/button2)"
        echo "  reset        Reset all configurations to device defaults"
        echo "  help         Show this help message or help for a specific subcommand"
        echo ""
        echo "Use '$0 help <subcommand>' for more details on a specific command."
    else
        case "$cmd" in
            theme)
                echo "Usage: $0 theme <light|dark|auto>"
                echo "Changes the device's UI mode."
                ;;
            density)
                echo "Usage: $0 density <DPI|reset>"
                echo "Changes the screen density. Use 'reset' to restore device default."
                ;;
            size)
                echo "Usage: $0 size <WIDTHxHEIGHT|reset>"
                echo "Changes the screen resolution. Use 'reset' to restore device default."
                ;;
            rotation)
                echo "Usage: $0 rotation <portrait|landscape|reverse-portrait|reverse-landscape>"
                echo "Changes the screen orientation (disables auto-rotate)."
                ;;
            locale)
                echo "Usage: $0 locale <LANG_CODE>"
                echo "Changes the device locale (e.g., en-US, es-ES). Requires Android 13+."
                ;;
            font-size)
                echo "Usage: $0 font-size <SCALE>"
                echo "Changes the font scale (e.g., 1.0, 1.2, 0.85)."
                ;;
            navigation)
                echo "Usage: $0 navigation <gestures|button3|button2>"
                echo "Changes the system navigation style."
                ;;
            reset)
                echo "Usage: $0 reset"
                echo "Resets theme, density, size, rotation, and font-size to defaults."
                ;;
            *)
                echo "Unknown subcommand: $cmd"
                show_help
                ;;
        esac
    fi
}

function set_theme() {
    local mode=$1
    case "$mode" in
        light)
            adb shell cmd uimode night no
            ;;
        dark)
            adb shell cmd uimode night yes
            ;;
        auto)
            adb shell cmd uimode night auto
            ;;
        *)
            echo "Error: Invalid theme mode '$mode'. Use light, dark, or auto."
            show_help theme
            exit 1
            ;;
    esac
}

function set_density() {
    local dpi=$1
    if [ "$dpi" == "reset" ]; then
        adb shell wm density reset
    elif [[ "$dpi" =~ ^[0-9]+$ ]]; then
        adb shell wm density "$dpi"
    else
        echo "Error: Invalid density '$dpi'. Must be a number or 'reset'."
        show_help density
        exit 1
    fi
}

function set_size() {
    local size=$1
    if [ "$size" == "reset" ]; then
        adb shell wm size reset
    elif [[ "$size" =~ ^[0-9]+x[0-9]+$ ]]; then
        adb shell wm size "$size"
    else
        echo "Error: Invalid size '$size'. Must be WIDTHxHEIGHT or 'reset'."
        show_help size
        exit 1
    fi
}

function set_rotation() {
    local mode=$1
    # First, disable auto-rotate
    adb shell settings put system accelerometer_rotation 0
    
    case "$mode" in
        portrait)
            adb shell settings put system user_rotation 0
            ;;
        landscape)
            adb shell settings put system user_rotation 1
            ;;
        reverse-portrait)
            adb shell settings put system user_rotation 2
            ;;
        reverse-landscape)
            adb shell settings put system user_rotation 3
            ;;
        *)
            echo "Error: Invalid rotation '$mode'."
            show_help rotation
            exit 1
            ;;
    esac
}

function set_locale() {
    local locale=$1
    if [ -z "$locale" ]; then
        echo "Error: Locale code required."
        show_help locale
        exit 1
    fi
    adb shell cmd activity set-locales "$locale"
}

function set_font_size() {
    local scale=$1
    if [[ "$scale" =~ ^[0-9]+(\.[0-9]+)?$ ]]; then
        adb shell settings put system font_scale "$scale"
    else
        echo "Error: Invalid font scale '$scale'. Must be a number (e.g., 1.2)."
        show_help font-size
        exit 1
    fi
}

function set_navigation() {
    local mode=$1
    case "$mode" in
        gestures)
            adb shell cmd overlay enable com.android.internal.systemui.navbar.gestural
            ;;
        button3)
            adb shell cmd overlay enable com.android.internal.systemui.navbar.threebutton
            ;;
        button2)
            adb shell cmd overlay enable com.android.internal.systemui.navbar.twobutton
            ;;
        *)
            echo "Error: Invalid navigation mode '$mode'. Use gestures, button3, or button2."
            show_help navigation
            exit 1
            ;;
    esac
}

function reset_all() {
    echo "Resetting device configurations..."
    adb shell cmd uimode night no
    adb shell wm density reset
    adb shell wm size reset
    adb shell settings put system accelerometer_rotation 1
    adb shell settings put system font_scale 1.0
    echo "Done."
}

# Main routing
cmd="$1"
shift

case "$cmd" in
    theme)
        if [[ "$1" == "--help" || "$1" == "-h" ]]; then
            show_help theme
        else
            set_theme "$1"
        fi
        ;;
    density)
        if [[ "$1" == "--help" || "$1" == "-h" ]]; then
            show_help density
        else
            set_density "$1"
        fi
        ;;
    size)
        if [[ "$1" == "--help" || "$1" == "-h" ]]; then
            show_help size
        else
            set_size "$1"
        fi
        ;;
    rotation)
        if [[ "$1" == "--help" || "$1" == "-h" ]]; then
            show_help rotation
        else
            set_rotation "$1"
        fi
        ;;
    locale)
        if [[ "$1" == "--help" || "$1" == "-h" ]]; then
            show_help locale
        else
            set_locale "$1"
        fi
        ;;
    font-size)
        if [[ "$1" == "--help" || "$1" == "-h" ]]; then
            show_help font-size
        else
            set_font_size "$1"
        fi
        ;;
    navigation)
        if [[ "$1" == "--help" || "$1" == "-h" ]]; then
            show_help navigation
        else
            set_navigation "$1"
        fi
        ;;
    reset)
        if [[ "$1" == "--help" || "$1" == "-h" ]]; then
            show_help reset
        else
            reset_all
        fi
        ;;
    help|--help|-h)
        show_help "$1"
        ;;
    *)
        if [ -z "$cmd" ]; then
            show_help
        else
            echo "Unknown command: $cmd"
            show_help
            exit 1
        fi
        ;;
esac
