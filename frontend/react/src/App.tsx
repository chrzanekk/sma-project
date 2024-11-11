import {Button} from '@/components/ui/button'
import { Theme } from "@chakra-ui/react"


function App() {
    return (
        <Theme appearance="light">
        <Button colorPalette={'green'} variant="surface">HIT ME</Button>
        </Theme>
    );
}

export default App;
