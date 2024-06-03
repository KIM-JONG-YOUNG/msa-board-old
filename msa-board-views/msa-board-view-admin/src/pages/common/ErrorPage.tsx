import { FallbackProps } from "react-error-boundary";

export default function ErrorPage({
	error,
	resetErrorBoundary
}: FallbackProps) {

	return (
		// <!-- Error -->
		<section className="resume-section">
			<div className="resume-section-content">
				<h1 className="mb-4">
					Error
					<span className="text-primary">Page</span>
				</h1>
			</div>
		</section>
	);
}