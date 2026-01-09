
import { parseISO, format } from 'date-fns';

class DateFormatter {
    static formatDateTime(isoString: string): string {
        if(!isoString) {
            return '';
        }
        try {
            const parsedDate = parseISO(isoString);
            return format(parsedDate, 'yyyy-MM-dd HH:mm:ss');
        } catch (error) {
            console.error('Error parsing date:', error);
            return 'Invalid Date';
        }
    }
    static formatDate(isoString: string): string {
        if(!isoString) {
            return '';
        }
        try {
            const parsedDate = parseISO(isoString);
            return format(parsedDate, 'dd-MM-yyyy');
        } catch (error) {
            console.error('Error parsing date:', error);
            return 'Invalid Date';
        }
    }
}

export default DateFormatter;
