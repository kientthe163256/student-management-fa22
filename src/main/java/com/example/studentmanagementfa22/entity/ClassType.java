package com.example.studentmanagementfa22.entity;

public enum ClassType {
    SUBJECT{
        @Override
        public String toString() {
            return "SUBJECT";
        }
    },
    SESSION{
        @Override
        public String toString() {
            return "SESSION";
        }
    }
}
