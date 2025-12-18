module stud_sluzba_desktop_client {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.fxml;

    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires spring.web;
    requires spring.webflux;
    requires static lombok;

    requires jasperreports;
    requires java.sql;
    requires java.desktop;

    requires reactor.core;
    requires org.reactivestreams;
    requires shared.dto;

    exports org.raflab.studsluzbadesktopclient;
    exports org.raflab.studsluzbadesktopclient.controllers;
    exports org.raflab.studsluzbadesktopclient.services;
    exports org.raflab.studsluzbadesktopclient.coder;

    opens org.raflab.studsluzbadesktopclient to javafx.fxml, spring.beans, spring.context, spring.core;
    opens org.raflab.studsluzbadesktopclient.controllers to javafx.fxml, spring.core;
    opens org.raflab.studsluzbadesktopclient.services to javafx.fxml, spring.core;
    opens org.raflab.studsluzbadesktopclient.coder to javafx.fxml, spring.beans, spring.context, spring.core;
}
