import {BaseEmployeeFormValues} from "@/types/employee-types.ts";
import React from "react";
import {useTranslation} from "react-i18next";
import {getSelectedCompany} from "@/utils/company-utils.ts";
import {PositionBaseDTO} from "@/types/position-types.ts";
import {getEmployeeValidationSchema} from "@/validation/employeeValidationSchema.ts";
import {addEmployee} from "@/services/employee-service.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import CommonEmployeeForm from "@/components/employee/CommonEmployeeForm.tsx";


interface AddEmployeeFormProps {
    onSuccess: (data: BaseEmployeeFormValues) => void;
}

const AddEmployeeForm: React.FC<AddEmployeeFormProps> = ({onSuccess}) => {
    const {t} = useTranslation(['common', 'errors', 'employees', 'positions']);
    const currentCompany = getSelectedCompany();

    const initialValues: BaseEmployeeFormValues = {
        firstName: '',
        lastName: '',
        hourRate: '',
        position: undefined as PositionBaseDTO | undefined,
        company: currentCompany
    }

    const validationSchema = getEmployeeValidationSchema(t);

    const handleSubmit = async (values: BaseEmployeeFormValues) => {
        try {
            const mappedEmployee: EmployeeDTO = {
                ...values,
                company: currentCompany,
                position: values.position!
            }
            const response = await addEmployee(mappedEmployee);
            successNotification(
                t('success', {ns: "common"}),
                formatMessage('notifications.addEmployeeSuccess', {
                    firstName: values.firstName,
                    lastName: values.lastName
                }, 'employees')
            )
            const mappedResponse: BaseEmployeeFormValues = {
                ...response,
            }
            onSuccess?.(mappedResponse);
        } catch (error: any) {
            errorNotification(
                t('error', {ns: "common"}),
                error.response?.data?.message || t('employees:notifications.addEmployeeError')
            );
        }
    };

    return (
        <CommonEmployeeForm initialValues={initialValues}
                            validationSchema={validationSchema}
                            onSubmit={handleSubmit}/>
    )
}
export default AddEmployeeForm;