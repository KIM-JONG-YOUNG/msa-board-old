import { FallbackProps } from "react-error-boundary";

export default function ErrorPage(prop: FallbackProps) {

    console.log(prop.error);

    return (
        // <!-- Error -->
		<section className="resume-section">
			<div className="resume-section-content">
				<h1 className="mb-4">
					Error
				</h1>
			</div>
		</section>
    );
}