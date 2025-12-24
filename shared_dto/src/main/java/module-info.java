module shared.dto {
    exports response;
    exports pomocniDTO;
    exports request;
    exports enums;
    requires jakarta.persistence;
    requires jakarta.validation;
    requires static lombok;
    requires org.jetbrains.annotations;

    opens response to com.fasterxml.jackson.databind;
    opens request to com.fasterxml.jackson.databind;
    opens enums to com.fasterxml.jackson.databind;
    opens pomocniDTO to com.fasterxml.jackson.databind;
}