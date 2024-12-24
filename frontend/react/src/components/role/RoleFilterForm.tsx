import {useTranslation} from "react-i18next";
import {Field, Form, Formik, FormikHelpers} from "formik";
import {Button, Grid, GridItem, Input} from "@chakra-ui/react";
import React from "react";
import * as Yup from 'yup';

interface FilterValues {
    name?: string;
}

const validationSchema = Yup.object({
    name: Yup.string().required()
});

interface Props {
    onSubmit: (values: FilterValues) => void;
}

const RoleFilterForm: React.FC<Props> = ({onSubmit}) => {
    const {t} = useTranslation('auth');
    return (
        <Formik
            initialValues={{name: ''}}
            validationSchema={validationSchema}
            onSubmit={(values, {setSubmitting}: FormikHelpers<FilterValues>) => {
                setSubmitting(false);
                onSubmit(values);
            }}
        >
            {({handleSubmit, isSubmitting}) => {
                return (
                    <Form onSubmit={handleSubmit}>
                        <Grid
                            templateColumns="repeat(3, 1fr)"
                            gap={4}
                        >
                            <GridItem colSpan={2}>
                                <Field
                                    name="name"
                                    as={Input}
                                    placeholder={t('roleName')}
                                />
                            </GridItem>
                            <GridItem colSpan={1}>
                                <Button
                                    type="submit"
                                    colorScheme="blue"
                                    isLoading={isSubmitting}
                                >
                                    {t('search')}
                                </Button>
                            </GridItem>
                        </Grid>
                    </Form>
                );
            }}
        </Formik>
    );
}

export default RoleFilterForm;