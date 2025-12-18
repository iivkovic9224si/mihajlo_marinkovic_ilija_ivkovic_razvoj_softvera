module shared.dto {
    exports response;
    exports pomocniDTO;
    exports request;
    requires jakarta.persistence;
    requires jakarta.validation;
    requires static lombok;
    requires org.jetbrains.annotations;
}