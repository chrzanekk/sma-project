import React, {useEffect, useState} from "react";
import {BaseEmployeeFormValues, EmployeeDTO, FetchableEmployeeDTO} from "@/types/employee-types.ts";
import {PositionBaseDTO} from "@/types/position-types.ts";
import {getEmployeeById, updateEmployee} from "@/services/employee-service.ts";
import {getEmployeeValidationSchema} from "@/validation/employeeValidationSchema.ts";
import {useTranslation} from "react-i18next";
import {getSelectedCompany} from "@/utils/company-utils.ts";
import {errorNotification, successNotification} from "@/notifications/notifications.ts";
import {formatMessage} from "@/notifications/FormatMessage.tsx";
import CommonEmployeeForm from "@/components/employee/CommonEmployeeForm.tsx";

interface EditEmployeeFormProps {
    onSuccess: (data: BaseEmployeeFormValues) => void;
    employeeId: number;
}

const EditEmployeeForm: React.FC<EditEmployeeFormProps> = ({onSuccess, employeeId}) => {

    const {t} = useTranslation(['common', 'employees', 'errors']);
    const currentCompany = getSelectedCompany();

    const defaultValues: BaseEmployeeFormValues = {
        firstName: '',
        lastName: '',
        hourRate: '',
        position: undefined as PositionBaseDTO | undefined,
        company: undefined
    }

    const [initialValues, setInitialValues] = useState<BaseEmployeeFormValues>(defaultValues);
    const [isLoading, setIsLoading] = useState<boolean>(true);

    useEffect(() => {
        const fetchEmployee = async () => {
            setIsLoading(true);
            try {
                const employee: FetchableEmployeeDTO = await getEmployeeById(employeeId)
                setInitialValues({
                    id: employee.id,
                    firstName: employee.firstName,
                    lastName: employee.lastName,
                    hourRate: employee.hourRate,
                    position: employee.position,
                    company: employee.company
                })
            } catch (err) {
                console.log(err);
            } finally {
                setIsLoading(false);
            }
        };
        fetchEmployee().catch();
    }, [employeeId]);

    const validationSchema = getEmployeeValidationSchema(t);

    const handleSubmit = async (values: BaseEmployeeFormValues) => {
        try {
            const mappedEmployee: EmployeeDTO = {
                ...values,
                position: values.position!,
                company: currentCompany!
            }
            const response = await updateEmployee(mappedEmployee);
            successNotification(
                t('success', {ns: "common"}),
                formatMessage('notifications.editEmployeeSuccess', {
                    firstName: values.firstName,
                    lastName: values.lastName
                }, 'employees')
            )
            const mappedResponse: BaseEmployeeFormValues = {
                ...response,
            }
            onSuccess?.(mappedResponse);
        } catch (err: any) {
            errorNotification(
                t('error', {ns: "common"}),
                err.response?.data?.message || t('employees:notifications.editEmployeeError')
            );
        }
    }

    return (
        <>{!isLoading && (
            <CommonEmployeeForm initialValues={initialValues}
                                validationSchema={validationSchema}
                                onSubmit={handleSubmit}/>
        )}
        </>
    )
};

export default EditEmployeeForm;


