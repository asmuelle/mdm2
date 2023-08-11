import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IOwnershipClassification } from '../ownership-classification.model';

@Component({
  standalone: true,
  selector: 'jhi-ownership-classification-detail',
  templateUrl: './ownership-classification-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class OwnershipClassificationDetailComponent {
  @Input() ownershipClassification: IOwnershipClassification | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
